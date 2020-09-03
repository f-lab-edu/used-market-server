package com.market.server.handler;
import com.market.server.utils.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@Log4j2
public class SocketHandler extends TextWebSocketHandler {

    private List<HashMap<String, Object>> rls = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트 ---roomListSessions

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        String rN = (String) obj.get("roomNumber");
        HashMap<String, Object> temp = new HashMap<String, Object>();

        if (rls.size() > 0) {
            for (int i = 0; i < rls.size(); i++) {
                String roomNumber = (String) rls.get(i).get("roomNumber"); //세션리스트의 저장된 방번호를 가져와서
                if (roomNumber.equals(rN)) { //같은값의 방이 존재한다면
                    temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
                    break;
                }
            }

            //해당 방의 세션들만 찾아서 메시지를 발송해준다.
            for (String k : temp.keySet()) {
                if (k.equals("roomNumber")) { //다만 방번호일 경우에는 건너뛴다.
                    continue;
                }

                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (IOException e) {
                        log.error("sendMessage 실패", e);
                    }
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        int fileUploadIdx = 0;
        //바이너리 메시지 발송
        ByteBuffer byteBuffer = message.getPayload();
        String fileName = DateUtil.getNowTimeToyyyyMMddHHmm(new Date(), ".jpg");
        String fileDirectoryName = System.getProperty("user.dir") + File.separator + "attachFile" + File.separator;
        File file = new File(fileDirectoryName, fileName);

        File dir = new File(fileDirectoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream out = new FileOutputStream(file, true); //생성을 위해 OutputStream을 연다.
             FileChannel outChannel = out.getChannel(); //채널을 열고)
        ) {
            byteBuffer.flip(); //byteBuffer를 읽기 위해 세팅
            byteBuffer.compact(); //파일을 복사한다.
            outChannel.write(byteBuffer); //파일을 쓴다.
        } catch (Exception e) {
            log.error("파일 전송 실패", e);
        }

        byteBuffer.position(0); //파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
        //파일쓰기가 끝나면 이미지를 발송한다.
        HashMap<String, Object> temp = rls.get(fileUploadIdx);
        for (String k : temp.keySet()) {
            if (k.equals("roomNumber")) {
                continue;
            }
            WebSocketSession wss = (WebSocketSession) temp.get(k);
            try {
                wss.sendMessage(new BinaryMessage(byteBuffer)); //초기화된 버퍼를 발송한다.
            } catch (IOException e) {
                log.error("sendMessage 실패", e);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        super.afterConnectionEstablished(session);
        boolean flag = false;
        String url = session.getUri().toString();
        System.out.println(url);
        String roomNumber = url.split("/chating/")[1];
        int idx = rls.size(); //방의 사이즈를 조사한다.
        if (rls.size() > 0) {
            for (int i = 0; i < rls.size(); i++) {
                String rN = (String) rls.get(i).get("roomNumber");
                if (rN.equals(roomNumber)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        if (flag) { //존재하는 방이라면 세션만 추가한다.
            HashMap<String, Object> map = rls.get(idx);
            map.put(session.getId(), session);
        } else { //최초 생성하는 방이라면 방번호와 세션을 추가한다.
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("roomNumber", roomNumber);
            map.put(session.getId(), session);
            rls.add(map);
        }

        //세션등록이 끝나면 발급받은 세션ID값의 메시지를 발송한다.
        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료
        if (rls.size() > 0) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
            for (int i = 0; i < rls.size(); i++) {
                rls.get(i).remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            log.error("jsonToObjectParser 실패", e);
        }
        return obj;
    }
}