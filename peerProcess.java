import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;import java.util.concurrent.ConcurrentHashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

class CommonConfigData {

    int preferredNumberOfNeighbours;
    int intervalOfUnchoking;
    int intervalOfOptimisticUnchoking;
    String myFileName;
    int fileSize;
    int chunkSize;

    public CommonConfigData() {
        this.preferredNumberOfNeighbours = 0;
        this.intervalOfUnchoking = 0;
        this.intervalOfOptimisticUnchoking = 0;

        this.fileSize = 0;
        this.chunkSize = 0;
    }

    public int getUnchokingInterval() {
        return intervalOfUnchoking;
    }

    public void parseAndUpdateData(ArrayList<String> rawDataRows) {
        if (rawDataRows.size() < 6) {
            // throw error, create custom exception
            System.out.println("Less number of rows");
            return;

        }
        updatePreferredNumberOfNeighbours(rawDataRows.get(0));
        updateIntervalOfUnchoking(rawDataRows.get(1));
        updateIntervalOfOptimisticUnchoking(rawDataRows.get(2));
        updateMyFileName(rawDataRows.get(3));
        updateFileSize(rawDataRows.get(4));
        updateChunkSize(rawDataRows.get(5));
    }

    public int getOptimisticUnchokingInterval() {
        return intervalOfOptimisticUnchoking;
    }

    private void updatePreferredNumberOfNeighbours(String key) {
        String[] words = key.split(" ");
        this.preferredNumberOfNeighbours = Integer.parseInt(words[1]);
    }

    private void updateIntervalOfUnchoking(String key) {
        String[] words = key.split(" ");
        this.intervalOfUnchoking = Integer.parseInt(words[1]);
    }

    private void updateIntervalOfOptimisticUnchoking(String key) {
        String[] words = key.split(" ");
        this.intervalOfOptimisticUnchoking = Integer.parseInt(words[1]);
    }

    private void updateMyFileName(String key) {
        String[] words = key.split(" ");
        this.myFileName = words[1];
    }

    private void updateFileSize(String key) {
        String[] words = key.split(" ");
        this.fileSize = Integer.parseInt(words[1]);
    }

    public int getFileSize() {
        return this.fileSize;
    }

    private void updateChunkSize(String key) {
        String[] words = key.split(" ");
        this.chunkSize = Integer.parseInt(words[1]);
    }

    public int getPieceSize() {
        return this.chunkSize;
    }

    public int getPreferredNumberOfNeighbours() {
        return preferredNumberOfNeighbours;
    }

    public String getMyFileName() {
        return myFileName;
    }

    public int calculateTotalChunks() {
        int totalChunks = (int) Math.ceil((double) this.getFileSize() / this.getPieceSize());
        return totalChunks;
    }

    public void printConfigDetails() {
        System.out.println("No. of preferred neighbors " + this.preferredNumberOfNeighbours);
        System.out.println("Unchoking Interval " + this.intervalOfUnchoking);
        System.out.println("Optimistic Unchoking Interval " + this.intervalOfOptimisticUnchoking);
        System.out.println("File Name " + this.myFileName);
        System.out.println("File Size " + this.fileSize);
        System.out.println("Piece Size " + this.chunkSize);
    }
}

class CommonConfigReader {

    String rootPath;

    public CommonConfigReader() {
        rootPath = System.getProperty("user.dir");
        rootPath = rootPath.concat("/");
    }

    public ArrayList<String> parseConfigFile(String fileName) throws IOException {
        String filePath = rootPath.concat(fileName);
        System.out.println(filePath);
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> rows = new ArrayList<>();
        for (Object row : br.lines().toArray()) {
            rows.add((String) row);
        }

        br.close();
        return rows;
    }

    public String getRootPath() {
        return this.rootPath;
    }

}

class MyLogger {

  Logger logger;
  FileHandler fileHandler;
  MyLogger(String peerId) throws IOException {
      logger = Logger.getLogger(peerProcess.class.getName());
      fileHandler = new FileHandler(".//"+ peerId +"//logs_"+ peerId+".log");
    //   fileHandler = new FileHandler(".//logs_"+ peerId +".log");
      fileHandler.setFormatter(new MyFormatter());
      logger.addHandler(fileHandler);
  }

  public void logInfo(String str){
      logger.log(new LogRecord(Level.INFO, str));
  }

  public void logError(String str){
      logger.log(new LogRecord(Level.SEVERE, str));
  }

  class MyFormatter extends Formatter {

      @Override
      public String format(LogRecord record) {
//            return record.getThreadID()+"::"+new Date(record.getMillis())+"::"+record.getMessage()+"\n";
          return new Date(record.getMillis())+" : "+record.getMessage()+"\n";
      }

  }
}

class GlobalMessageTypes {
    private HashMap<String, Character> map;

    public GlobalMessageTypes() {
        map = new HashMap<>();
        map.put("UNCHOKE" , '1');
        map.put("BITFIELD" , '5');
        map.put("INTERESTED" , '2');
        map.put("NOT_INTERESTED" , '3');
        map.put("REQUEST" , '6');
        map.put("PIECE" , '7');
        map.put("HAVE" , '4');
        map.put("CHOKE" , '0');
    }

    public char getChokeChar() {
        char ch = map.get("CHOKE");
        return ch;
    }

    public char getUnchokeChar() {
        char ch = map.get("UNCHOKE");
        return ch;
    }

    public char getInterestedChar() {
        char ch = map.get("INTERESTED");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }

    public char getNotInterestedChar() {
        char ch = map.get("NOT_INTERESTED");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }

    public char getBitFieldChar() {
        char ch = map.get("BITFIELD");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }
    public char getRequestChar() {
        char ch = map.get("REQUEST");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }
    public char getPeiceChar() {
        char ch = map.get("PIECE");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }

    public char getHaveChar() {
        char ch = map.get("HAVE");
        System.out.println(ch + " -----------------------------------------------------");
        return ch;
    }
}
class GlobalConstants {

    private final static String TORRENT_FILE = "thefile";
    private final static String COMMON_CFG_FILE_NAME = "Common.cfg";
    private final static String PEER_INFO_FILE_NAME = "PeerInfo.cfg";
    private final static String handShakeHeader = "P2PFILESHARINGPROJ";
    private final static String tenZeros = "0000000000";


    public static String getHandShakeHeader() {
        return handShakeHeader;
    }

    public static String getTenZeros() {
        return tenZeros;
    }

    public static String getTorrentFileName() {
        return TORRENT_FILE;
    }

    public static String getCommonConfigFileName() {
        return COMMON_CFG_FILE_NAME;
    }

    public static String getPeerInfoFileName() {
        return PEER_INFO_FILE_NAME;
    }
}

class GlobalHelperFunctions {

    public static String extractString(byte[] byteArray, int startIndex, int endIndex) {
        int newLength = endIndex - startIndex + 1;
        //tbd - exception
        if (newLength <= 0 || endIndex >= byteArray.length) return "";
        byte[] copy = new byte[newLength];
        System.arraycopy(byteArray, startIndex, copy, 0, newLength);
        return new String(copy, StandardCharsets.UTF_8);
    }

    public static byte[] generateHandshakePacket(int peerId) {
        byte[] hSPacket = new byte[32];

        byte[] headerInBytes = GlobalConstants.getHandShakeHeader().getBytes();
        byte[] zerosInBytes = GlobalConstants.getTenZeros().getBytes();
        byte[] peerIdInBytes = ByteBuffer.allocate(4).put(String.valueOf(peerId).getBytes()).array();
        int index = 0;

        for (int i = 0; i < headerInBytes.length; i++) {
            hSPacket[index++] = headerInBytes[i];
        }

        for (int i = 0; i < zerosInBytes.length; i++) {
            hSPacket[index++] = zerosInBytes[i];
        }

        for (int i = 0; i < peerIdInBytes.length; i++) {
            hSPacket[index++] = peerIdInBytes[i];
        }

        return hSPacket;
    }

    public static byte[] copyByteArray(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static boolean checkMissingChunksInMe(int[] thisPeerBitfield, int[] connectedPeerBitfield, int len) {
        int i;
        for (i = 0; i < len; i++) {
            if (thisPeerBitfield[i] == 0 && connectedPeerBitfield[i] == 1) {
                return true;
            }
        }
        return false;
    }

    public static int getRandomFileChunk(int[] thisPeerBitfield, int[] connectedPeerBitfield, int len) {
        ArrayList<Integer> chunksneededByMe = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (thisPeerBitfield[i] == 0 && connectedPeerBitfield[i] == 1) {
                chunksneededByMe.add(i);
            }
        }
        int chunksNeedLen = chunksneededByMe.size();

        if (chunksNeedLen <= 0) {
            // You dont need anything, everything is cool.
            return -1;
        } else {
            Random randObj = new Random();
            int randIndex = Math.abs(randObj.nextInt() % chunksNeedLen);
            int ans = chunksneededByMe.get(randIndex);
            return ans;
        }
    }

    // Added logging functions here

    public static void logger(BufferedWriter writer, int id1, int id2, String message_type) {
        Date time;
        time = new Date();

        StringBuffer write_log;
        write_log = new StringBuffer();
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        if (message_type == "CHOKE") {
            write_log.append(timeFormat.format(time)+": Peer [" + id1  +"] is choked by ["+ id2 +"].") ;

        } else if (message_type == "UNCHOKE") {
            write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] is unchoked by [" + id2 + "].");

        } else if (message_type == "connectionTo") {
            write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] makes a connection to Peer [" + id2 + "].");

        } else if (message_type == "connectionFrom") {
            write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] is connected from Peer [" + id2 + "].");

        } else if (message_type == "changeOptimisticallyUnchokedNeighbor") {
            write_log.append(timeFormat.format(time)+": Peer [" + "] has the optimistically unchoked neighbor [" + id2 + "].");


        } else if (message_type == "INTERESTED") {
            write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] received the 'interested' message from [" + id2 + "]." );

        } else if (message_type == "NOTINTERESTED") {
            write_log.append(timeFormat.format(time)+": Peer [" +id1 + "] received the 'not interested' message from [" + id2 + "]." );

        }
        try {
            String final_value;
            final_value = write_log.toString() ;
            writer.write(final_value);
            writer.newLine();
            writer.flush();
        }catch(FileNotFoundException e){

        }catch(IOException e){

        }


    }

    public static void logger_receive_have(BufferedWriter writer, int id1, int id2, int index) {

        Date time;
        time = new Date();

        StringBuffer write_log;
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        write_log = new StringBuffer();
        write_log.append(timeFormat.format(time) + ": Peer [" + id1 +"] received 'have' message from [" + id2+ "] for the piece: " + index + '.' );
        try {
            String final_value;
            final_value = write_log.toString() ;
            writer.write(final_value);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static void logger_change_preferred_neighbors(BufferedWriter writer, int id1, int[] id_list) {

        Date time;
        time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        StringBuffer write_log;
        write_log = new StringBuffer();
        write_log.append(timeFormat.format(time) +": Peer [" + id1 + "] has the preferred neighbors [" );

        for (int i = 0;i <id_list.length ; i++) {
            write_log.append(id_list[i]);
            if (i < (id_list.length - 1) )
                write_log.append(',');

        }
        //writer_log.deleteCharAt(writer_log.length() - 1);
        write_log.append("].");
        try {
            String final_value;
            final_value = write_log.toString() ;
            writer.write(final_value);
            writer.newLine();
            writer.flush();
        } catch(FileNotFoundException e){

        }catch(IOException e){

        }
    }

    public static void logger_download_piece(BufferedWriter writer, int id1, int id2, int index, int number_of_pieces) {
        Date time;
        time = new Date();

        StringBuffer write_log;
        write_log = new StringBuffer();
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        write_log.append(timeFormat.format(time) + ": Peer [" + id1 + "] has downloaded the piece " + index + " from [" + id2 + "]. " +"Now the number of pieces it has is : "+ number_of_pieces + '.');

        try {
            String final_value;
            final_value = write_log.toString() ;
            writer.write(final_value);
            writer.newLine();
            writer.flush();
        } catch(FileNotFoundException e){

        }catch(IOException e){

        }
    }

    public static void logger_completed_downloading(BufferedWriter writer, int id1) {
        Date time;
        time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        StringBuffer write_log;
        write_log = new StringBuffer();
        write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] has downloaded complete file ");

        try {
            String final_value;
            final_value = write_log.toString() ;
            writer.write(final_value);
            writer.newLine();
            writer.flush();
        } catch(FileNotFoundException e){

        }catch(IOException e){

        }
    }
    // logging functions end

}



class ConnectedPeerNode {
    // private boolean haveFile;
    private int peerId;
    private String nameOfHost;
    private int portNumber;
    private int hasFile;
    private int[] pieceMarker;
    private int numberOfPieces;

    public ConnectedPeerNode() {
        this.numberOfPieces = 0;
    }

    public ConnectedPeerNode(int peerId, String nameOfHost, int portNumber, int hasFile) {
        this.peerId = peerId;
        this.nameOfHost = nameOfHost;
        this.portNumber = portNumber;
        this.hasFile = hasFile;
        this.numberOfPieces = 0;
    }

    public int initializePeerObject(String rawPeerData) {
        String[] words = rawPeerData.split(" ");
        this.peerId = Integer.parseInt(words[0]);
        this.nameOfHost = words[1];
        this.portNumber = Integer.parseInt(words[2]);
        this.hasFile = Integer.parseInt(words[3]);

        return this.peerId;
    }

    public boolean hasFile() {

        if (this.hasFile == 1) {
            return true;
        }

        return false;
    }

    public void setHaveFile(int haveFile) {
        this.hasFile = haveFile;
    }

    public void putFileOne() {
        this.hasFile = 1;
    }


    public void putFileZero() {
        this.hasFile = 0;
    }

    public int getPeerId() {
        return this.peerId;
    }


    public void downCompOne() {
        this.hasFile = 1;
    }

    public void downCompZero() {
        this.hasFile = 0;
    }

    public void fileDownloaded() {
        this.hasFile = 1;
    }

    public int getHasFile() {
        return this.hasFile;
    }

    public boolean updateHasFile(int val) {
        this.hasFile = val;
        return this.hasFile == 1;
    }

    public void setPieceMarker(int[] pieceMarker) {
        this.pieceMarker = pieceMarker;
    }

    public void updateChunkMarker(int[] chunkMarker) {
        System.out.println("updating bit field for " + this.peerId);
        this.pieceMarker = chunkMarker;
    }

    public int getStoredChunks() {
        int count = 0;
        for (int i = 0; i < pieceMarker.length; i++) {
            if (pieceMarker[i] == 1)
                count++;
        }

        return count;
    }

    public int getChunksLength() {
        return this.pieceMarker.length;
    }

    public void updateNumOfPieces() {
        this.numberOfPieces++;

        // check if all the chunks have been received.
        if (this.numberOfPieces == this.pieceMarker.length) {
            this.hasFile = 1;
        }
    }

    public int getNumOfPieces() {
        return this.numberOfPieces;
    }

    public int getportNumber() {
        return this.portNumber;
    }

    public String getNameOfHost() {
        return this.nameOfHost;
    }

    public int[] getBitField() {
        return this.pieceMarker;
    }

    public void updateBitfield(int index) {
        this.pieceMarker[index] = 1;
    }

    @Override
    public String toString() {
        return (" peerId " + this.peerId + " hostName: " + this.nameOfHost + " portNumber: " + this.portNumber
                + " hasFile: " + this.hasFile + "\n");
    }
}



public class peerProcess {
    static GlobalMessageTypes globalMessageTypes;
    static CommonConfigReader globalConfigReader;
    static CommonConfigData commonConfigData;
    static LinkedHashMap<Integer, ConnectedPeerNode> peerMap;
    static int currentNodeId;
    static ConnectedPeerNode currentNode;
    static byte[][] currentFilePieces;
    static int peersWithCompleteFiles = 0;
    static File currentNodeDir;
    private static ConcurrentHashMap<Integer, AdjacentConnectionNode> connectionsMap;
    private static File log_file;
    static MyLogger logger;
    private static String torFileName;

    private static class MainBackgroundThread extends Thread {
        private AdjacentConnectionNode peer;

        public MainBackgroundThread(AdjacentConnectionNode peer) {
            this.peer = peer;
        }

        public void showDownloadData() {
            int chunksTillNow = currentNode.getNumOfPieces();
            int totalChunks = commonConfigData.calculateTotalChunks();
            double downloadedPercentage = ((chunksTillNow * 100.0)/totalChunks);
            String outputRes = chunksTillNow + "/" + totalChunks + " downloaded: " + downloadedPercentage +"% ";
            System.out.println(outputRes);
        }

        @Override
        public void run() {
            synchronized (this) {

                try {
                    ObjectInputStream inputStream = new ObjectInputStream(peer.getConnection().getInputStream());
                    System.out.println("Sending bit field msg ... ");

                    /*DataInputStream dataInputStream = new DataInputStream(peer.getConnection().getInputStream());
                    System.out.println("Sending bit field msg ... ");*/
                    peer.sendBitFieldMsg();
                    int ccc = 0;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));

                    while (peersWithCompleteFiles < peerMap.size()) {
                        /*int msgLength = dataInputStream.readInt();*/
                        int msgLength = inputStream.readInt();

                        byte[] msg = new byte[msgLength - 1];
                        byte[] inputMsg = new byte[msgLength];

                        double startTime = (System.nanoTime() / 100000000.0);
                        /*dataInputStream.readFully(inputMsg);*/
                        inputStream.readFully(inputMsg);
                        double endTime = (System.nanoTime() / 100000000.0);

                        char msgType = (char) (inputMsg[0]);
                        int index = 0;
                        for (int i = 1; i < msgLength; i++) {
                            msg[index++] = inputMsg[i];
                        }

                        writer.flush();

                        if (msgType == globalMessageTypes.getBitFieldChar()) {
                            // waale functio mein hun");
                            int inde = 0;

                            int[] bitfield = new int[msg.length / 4];
                            for (int i = 0; i < msg.length; i += 4) { //
                                byte[] tempByteArray = GlobalHelperFunctions.copyByteArray(msg, i, i + 4);
                                bitfield[inde++] = ByteBuffer.wrap(tempByteArray).getInt();
                            }

                            // update the bitfield/chunks
                            ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());
                            connectedPeerObject.updateChunkMarker(bitfield);
                            int currentPeerChunks = connectedPeerObject.getStoredChunks();

                            if (currentPeerChunks == currentNode.getChunksLength()) {
                                boolean ans = connectedPeerObject.updateHasFile(1);
                                peersWithCompleteFiles++;
                            } else {
                                boolean ans = connectedPeerObject.updateHasFile(0);
                            }

                            boolean checkMissingChunksInMe = GlobalHelperFunctions.checkMissingChunksInMe(currentNode.getBitField(),
                                    connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                            // System.out.println("Kya " + currentNodeId + " mere chunks missing hain ?" +
                            // checkMissingChunksInMe + " ? ");
                            // set interested msg.

                            if (checkMissingChunksInMe) {
                                // System.out.println("iss connection pe interested tick laga do");
                                peer.sendInterestedMessage(); // mein iss waale connection say interested hun lenen kay liye, toh ye
                                // connection kee property hain.
                            } else {
                                // System.out.println("Mujhe iss connection say kuch nai chaiye");
                                peer.sendNotInterestedMsg() ;
                            }

                        } else if (msgType == globalMessageTypes.getInterestedChar()) {
                            System.out.println("Mera peer mere say lene mein interested hain.... last wala");
                            peer.fetchIntres();
                            GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.nodeId, "INTERESTED");

                        } else if (msgType == globalMessageTypes.getNotInterestedChar()) {
                            System.out.println("Meer peer mujhe bol rha hain, kee vo interested nai hain ,,,, last wala);");
                            peer.putNotIntres();
                            GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.nodeId, "NOTINTERESTED");

                            // System.out.println("Server say msg .... CHOKCET status ");
                            if (!peer.isChoked()) {
                                System.out.println("Mere peer choked nai hain,, ab usse choke kar do");
                                peer.chokeConnection();
                                peer.sendChokeMessage();
                            }
                        } else if (msgType == globalMessageTypes.getUnchokeChar()) {
                            peer.unChokeConnection();
                            GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.nodeId, "UNCHOKE");

                            System.out.println(peer.getPeerId() + " is unchoked on rcv side");
                            // System.out.println("Connection unchoked....");

                            // Request peice from your sender now.
                            ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());

                            int randomChunkIndex = GlobalHelperFunctions.getRandomFileChunk(currentNode.getBitField(),
                                    connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                            if (randomChunkIndex == -1) {
                                System.out.println("Nothing found, maybee there is no file I (RECEIVER) dont want to haves");
                                System.out.println(peer.isChoked() + " <----- " + peer.isInterested());
                            } else {
                                // System.out.println("Now, I(RECEIVER) will request for the chunk after
                                // unchoking");
                                peer.sendRequestMessage(randomChunkIndex);
                            }

                        } else if (msgType == globalMessageTypes.getRequestChar()) {
                            int fileChunkIndex = ByteBuffer.wrap(msg).getInt();
                            peer.sendPieceMessage(fileChunkIndex);
                        } else if (msgType == globalMessageTypes.getPeiceChar()) {
                            System.out.println("PIECE rcv from " + peer.getPeerId());

                            {

                                // System.out.println("Ye index mila " + recvFileChunkIndex);

                                int recvFileChunkIndex = ByteBuffer.wrap(GlobalHelperFunctions.copyByteArray(msg, 0, 4)).getInt();
                                ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());
                                currentFilePieces[recvFileChunkIndex] = new byte[msg.length - 4];

                                int ci = 0;
                                for (int i = 4; i < msg.length; i++) {
                                    byte[] currFileRow = currentFilePieces[recvFileChunkIndex];
                                    currFileRow[ci++] = msg[i];
                                }

                                currentNode.updateBitfield(recvFileChunkIndex);

                                currentNode.updateNumOfPieces();

                                if (peer.isChoked() == false) {

                                    int randomChunkIndex = GlobalHelperFunctions.getRandomFileChunk(currentNode.getBitField(),
                                            connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                                    if (randomChunkIndex == -1) {
                                        // System.out.println("Nothing found, maybee there is no file I (RECEIVER) want
                                        // to fetch");
                                        // System.out.println("Hasfile variable " + currentNode.hasFile());
                                    } else {
                                        // System.out.println("Now, I(RECEIVER) will request for the another chunk after
                                        // unchoking with " + randomChunkIndex);
                                        peer.sendRequestMessage(randomChunkIndex);
                                    }
                                } else {
                                    // System.out.println("PEER RCVED PEICE IN CHOKED STATE");
                                }
                                double rate = ((double) (msg.length + 5) / (endTime - startTime));
                                int hasFile = connectedPeerObject.getHasFile();
                                if (hasFile == 1) {
                                    // the file is completed..
                                    // peer.DownloadSpeed(-1);
                                    peer.setDownloadSpeed(-1);
                                } else {
                                    //
                                    peer.setDownloadSpeed(rate);
                                }
                                GlobalHelperFunctions.logger_download_piece(writer, currentNode.getPeerId(), peer.getPeerId(),
                                        recvFileChunkIndex, currentNode.getNumOfPieces());


                                showDownloadData();
                                peer.checkCompleted(recvFileChunkIndex);
                                // Now check have msg ...
                                // System.out.println("hello -- bro -----, I am coming back to peices -- " +
                                // currentNode.hasFile());

                                for (int connection : connectionsMap.keySet()) {
                                    AdjacentConnectionNode pc = connectionsMap.get(connection);
                                    pc.sendHaveMessage(recvFileChunkIndex);
                                }
                                //
                            }
                        } else if (msgType == globalMessageTypes.getHaveChar()) {
                            int haveChunkIndex = ByteBuffer.wrap(msg).getInt();
                            ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());

                            connectedPeerObject.updateBitfield(haveChunkIndex);

                            // now check if
                            int totalBits = connectedPeerObject.getStoredChunks();
                            if (totalBits == currentNode.getChunksLength()) {
                                connectedPeerObject.setHaveFile(1);
                                peersWithCompleteFiles++;
                            }

                            {
                                // IMPORTANT: Yaha par mein ye
                                boolean checkMissingChunksInMe = GlobalHelperFunctions.checkMissingChunksInMe(currentNode.getBitField(),
                                        connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                                if (checkMissingChunksInMe) {
                                    peer.sendInterestedMessage(); // mein iss waale connection say interested hun
                                } else {
                                    peer.sendNotInterestedMsg() ;
                                }

                            }
                            GlobalHelperFunctions.logger_receive_have(writer, currentNode.getPeerId(), peer.getPeerId(), haveChunkIndex);

                        } else if (msgType == globalMessageTypes.getChokeChar()) {
                            GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.nodeId, "CHOKE");

                            peer.chokeConnection();
                            System.out.println("PEER is choked");
                        } else {
                            System.out.println("KUCH PANGA HAI");
                            ccc++;
                        }

                    }

                    System.out.println("bhaiya meient oh khatam ho gya hun....");

                    writer.close();
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
            }
        }
    }

    private static class AdjacentConnectionNode {
	  

        private double downRate = 0;
        private boolean isOptimisticUnchoke = false;
        private Socket connection;
        private int nodeId;
        private boolean interest = false;
        private boolean choke = true;
    
        public AdjacentConnectionNode(Socket connection, int nodeId) {
          this.connection = connection;
          this.nodeId = nodeId;
          (new MainBackgroundThread(this)).start();
    
        }
    
        public double getDownloadSpeed() {
          return downRate;
        }
    
        public void setDownloadSpeed(double downRate) {
          this.downRate = downRate;
        }
    
        public boolean isChoked() {
          return this.choke;
        }
    
        public void chokeConnection() {
          this.choke = true;
        }
    
        public boolean isOptimisticUnchoke() {
          return isOptimisticUnchoke;
        }
    
        public void unChokeConnection() {
          this.choke = false;
        }
    
        public void optimisticallyUnchoke() {
          isOptimisticUnchoke = true;
        }
    
        public void optimisticallyChoke() {
          isOptimisticUnchoke = false;
        }
    
        public boolean isInterested() {
          return this.interest;
        }
    
        public void putNotIntres() {
          this.interest = false;
        }
    
        public void fetchIntres() {
          this.interest = true;
        }
    
        public Socket getConnection() {
          return this.connection;
        }
    
        public int getPeerId() {
          return this.nodeId;
        }
    
        public boolean check() { return true;}
    
        public byte[] getFileChunk(int pieceIndex, byte[] piece) {
          int index = 0;
          int pieceLength = piece.length;
          byte[] load = new byte[pieceLength+4];
    
          byte[] indexBytes = ByteBuffer.allocate(4).putInt(pieceIndex).array();
          int j = 0;
          while (j < indexBytes.length) {
            load[index] = indexBytes[j];
            j = j + 1;
            index = index + 1;
          }
          j = 0;
          while (j < pieceLength) {
            load[index] = piece[j];
            j = j + 1;
            index = index + 1;
          }
          boolean check = check();
          byte[] returnPacket = null;
          
          
          try {
            if(check) {
              int totalLength = pieceLength + 5;
              char type = globalMessageTypes.getPeiceChar();
              returnPacket =  createPacket(totalLength, type, load);
            }
    
          } catch(WrongPacketException e) {
              System.out.println(e.getMessage());
              System.exit(0);
          }
          
          return returnPacket;
        }
    
        public byte[] createPacket(int length, char messageType, byte[] payload) throws WrongPacketException {
          
          if (messageType == globalMessageTypes.getInterestedChar() || messageType == globalMessageTypes.getNotInterestedChar() || messageType == globalMessageTypes.getUnchokeChar() || messageType == globalMessageTypes.getChokeChar()) {
    
              int index = 0;
              byte type = (byte) messageType;
              byte[] returnPacket = new byte[length + 4];
              byte[] header = ByteBuffer.allocate(4).putInt(length).array();
              int checkLength = header.length;
              int j = 0;
              while (j<checkLength) {
                byte m = header[j];
                returnPacket[index] = m;
                index = index+1;
                j = j + 1;
              }
              returnPacket[index] = type;
              return returnPacket;
          } else if (messageType == globalMessageTypes.getBitFieldChar() || messageType == globalMessageTypes.getRequestChar() || messageType == globalMessageTypes.getPeiceChar() || messageType == globalMessageTypes.getHaveChar()) {
    
              int index = 0;
              byte msgType = (byte) messageType;
              byte[] resultPacket = new byte[length + 4];
              byte[] header = ByteBuffer.allocate(4).putInt(length).array();
              int checkLength = header.length;
              int checkLength1 = payload.length;
              int i = 0;
              while(i<checkLength){
                byte m = header[i];
                resultPacket[index] = m;
                index = index+1;
                i = i + 1;
              }
              resultPacket[index++] = msgType;
              int j = 0;
              while(j<checkLength1){
                byte m = payload[j];
                resultPacket[index] = m;
                index = index + 1;
                j = j + 1;
              }
              
              return resultPacket;
          } else {
              System.out.println(" Wrong packet messageType provided");
              throw new WrongPacketException("Wrong packet request " + messageType);
          }
          
          
        }
    
        public void sendHaveMessage(int pieceIndex) {
    
          try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.flush();
    
            byte[] load = ByteBuffer.allocate(4).putInt(pieceIndex).array();
            byte[] haveMessage = null;
    
            try {
              haveMessage = createPacket(5, globalMessageTypes.getHaveChar(), load);
            } catch(WrongPacketException e) {
              System.out.println(e.getMessage());
              System.exit(0);
            }
            outputStream.write(haveMessage);
            outputStream.flush();
    
          }  catch (IOException exception) {
            System.out.println("Error occurred while sending the have message");
          }
        }
    
        public void sendRequestMessage(int index) {
            try {
                DataOutputStream outputStream  = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                byte[] load = ByteBuffer.allocate(4).putInt(index).array();
                byte[] requestMessage = null;
                try {
                  requestMessage = createPacket(5, globalMessageTypes.getRequestChar(), load);
                } catch(WrongPacketException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                outputStream.write(requestMessage);
                outputStream.flush();
    
            }  catch (IOException e) {
                System.out.println("Error sending request message");
            } finally {
                System.out.println("Message request completed");
            }
    
        }
        
        public void sendPieceMessage(int index) {
            
            try {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                outputStream.write(getFileChunk(index, currentFilePieces[index]));
                outputStream.flush();
    
            }  catch (IOException exception) {
                System.out.println("Piece message sending failed");
            }
        }
    
        public void sendInterestedMessage() {
          try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.flush();
    
    
            byte[] interestedMessage = null;
            try {
              interestedMessage = createPacket(1, globalMessageTypes.getInterestedChar(), null);
            } catch(WrongPacketException e) {
              System.out.println(e.getMessage());
              System.exit(0);
            }
            outputStream.write(interestedMessage);
            outputStream.flush();
    
          } catch (IOException e) {
            System.out.println("Error occurred while writing the output stream when sending the interested message");
          }
        }
    
    
        
    
        
    
        
        public void sendBitFieldMsg() {
             try{
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                int[] bitField = currentNode.getBitField();
                int totalLength = currentNode.getBitField().length;
                int newMessageLength = (4 * totalLength)+ 1;
                byte[] load = new byte[newMessageLength - 1];
                int index = 0;
                for (int j=0;j < totalLength;j++) {
                int number = bitField[j];
                  byte[] numberByteArray = ByteBuffer.allocate(4).putInt(number).array();
                  int checkLength = numberByteArray.length;
                    for (int k=0;k < checkLength;k++) {
                        byte oneByte = numberByteArray[k];
                        load[index] = oneByte;
                        index = index + 1;
                  }
                }
                
                byte[] bitMessage = null;
                
                try {
                    bitMessage = createPacket(newMessageLength, globalMessageTypes.getBitFieldChar(), load);
                } catch(WrongPacketException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                outputStream.write(bitMessage);
                outputStream.flush();
            } catch(IOException exception){
                System.out.println("Error occurred while writing the output stream while sending teh bit field message");
            }
        }
        
        public void sendNotInterestedMsg() {
            try {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                byte[] iamNotInterestedMessage = null;
                
                try {
                    iamNotInterestedMessage = createPacket(1, globalMessageTypes.getNotInterestedChar(), null);
                } catch(WrongPacketException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                
                outputStream.write(iamNotInterestedMessage);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Error occurred while creating the packet while sending the not interested message");
            }
        }
        
        
        public void sendChokeMessage() {
            try {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                byte[] chokeMessage = null;
                
                try {
                    chokeMessage = createPacket(1, globalMessageTypes.getChokeChar(), null);
               } catch(WrongPacketException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                
                outputStream.write(chokeMessage);
                outputStream.flush();
    
            } catch (IOException exception) {
                System.out.println("Error occurred while writing the choke message packet");
            }
        }
        
        
        public void sendUnChokeMessage() {
            
            try {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.flush();
                byte[] unchokeMessage = null;
                try {
                    unchokeMessage = createPacket(1, globalMessageTypes.getUnchokeChar(), null);
               } catch(WrongPacketException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                outputStream.write(unchokeMessage);
                outputStream.flush();
    
            } catch (IOException e) {
                System.out.println("Error occurred while writing the output stream when sending the unchoke messsage");
            }
        }
    
        private  void writeToFile(String filePath, byte[] newFile) throws IOException{
            try {
    
              BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
              outputStream.write(newFile);
              outputStream.close();
    
            } catch (IOException e){
              System.out.println("Error occurred while writing the final file");
            }
    
          }
        
        public void checkCompleted(int recvFileChunkIndex) {
            int totalNumberOfPieces = 0;
            int[] currentNodeBitFields = currentNode.getBitField();
            int checkLength = currentNodeBitFields.length;
            int k = 0;
            while (k<checkLength) {
              if (currentNodeBitFields[k] == 1) {
                totalNumberOfPieces = totalNumberOfPieces + 1;
              }
              k = k + 1;
            }
    
            
          if (totalNumberOfPieces == checkLength) {
            logger.logInfo("Peer " + currentNode.getPeerId() + " has completed downloading file");
            // try { 
            //     BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
            //     GlobalHelperFunctions.logger_completed_downloading(writer, currentNode.getPeerId());
            //     writer.close();
            // } catch (IOException e1){
            //     e1.printStackTrace();
    
            // }
            
            int index = 0;
            int fileSize = commonConfigData.getFileSize();
            byte[] newFile = new byte[fileSize];
            int checkLength1 = currentFilePieces.length;
            for (int i=0;i < checkLength1;i++) {
              int checkLength2 =  currentFilePieces[i].length;
                for (int j=0; j < checkLength2;j++) {
                    byte currentByte = currentFilePieces[i][j];
                    newFile[index] = currentByte;
                    index = index + 1;
                  }
            }
            try {
              
              String finalFilePath = globalConfigReader.getRootPath() + "/" + currentNodeId + "/" + (torFileName);
              
              writeToFile(finalFilePath, newFile);
              currentNode.fileDownloaded();
              peersWithCompleteFiles += 1;
    
            } 
            catch (Exception exception) {
              exception.printStackTrace();
            } 
            } 
            else {
              int tempIndex = 0;
              int resultChunkSize = commonConfigData.getPieceSize();
              byte[] newChunkInBytes = new byte[resultChunkSize];
                  for (int j=0; j < currentFilePieces[recvFileChunkIndex].length;j++) {
                      byte currByte = currentFilePieces[recvFileChunkIndex][j];
                      newChunkInBytes[tempIndex++] = currByte;
                }
              
              System.out.println("Writing chunk to the folder.... " + recvFileChunkIndex);
              
              try {
                  String finalFilePath = globalConfigReader.getRootPath() + "/peer_" + currentNodeId + "/" + ("chunk_" + torFileName + "_" + recvFileChunkIndex);
                  writeToFile(finalFilePath, newChunkInBytes);
    
                } catch (Exception e) {
                  e.printStackTrace();
                }
              
              
          }
        }
    
      }



    private static class ServerThreadRunnable implements Runnable {

        public boolean isNumber(String s) {

            if (s.isEmpty()) return false;

            s = s.trim();
            int n = s.length();

            if (n == 0) return false;
            if (n == 1) return isD(s.charAt(0));

            int i = 0;
            int j = n - 1;


            int dot = 0;
            int exp = 0;
            int sign = 0;

            for(i = 0; i < n; i++) {
                char c = s.charAt(i);
                if (c == '.') {
                    dot++;
                }
                else if (c == 'e') {
                    exp++;
                }
                else if (isSign(c)) {
                    sign++;
                } else {
                    if (! isD(c)) return false;
                }

            }

            if (exp > 1 || dot > 1 || sign > 2) return false;

            if (exp == 1) {
                i = s.indexOf('e');
                return isExp(i+1, n-1, s) && isDecimal(0, i-1, s);
            }

            if (dot == 1) {
                return isDecimal(0, n-1, s);
            }

            //all digits with one leading sigh allowed
            if (isSign(s.charAt(0))) return allD(1, n-1,s);
            return allD(0, n-1, s);


        }

        private boolean isDecimal(int i, int j, String s) {
            int n = s.length();
            if (i < 0 || j >= n) return false;
            if (i > j) return false;

            if (isSign(s.charAt(i))) return isDecimal(i+1, j, s);

            int k = i;
            for(;k <= j; k++) if (s.charAt(k) == '.') break;

            if (k > j) return allD(i, j, s);
            if (k == i) return allD(i+1, j, s);
            if (k == j) return allD(i, j- 1, s);

            return allD(i, k-1, s) && allD(k+1, j, s);
        }


        private boolean isExp(int i, int j, String s) {
            int n = s.length();
            if (i < 0 || j >= n) return false;
            if (i > j) return false;

            if (isSign(s.charAt(i))) return allD(i+1,j, s);

            return allD(i, j, s);
        }

        private boolean allD(int i, int j, String s) {
            int n = s.length();
            if (i < 0 || j >= n) return false;
            if (i > j) return false;
            while(i <= j) {
                if (! isD(s.charAt(i))) return false;
                i++;
            }
            return true;
        }

        private boolean isD(char c) {
            return c >= '0' && c <= '9';
        }

        private boolean isSign(char c) {
            return c == '-' || c == '+';
        }

        @Override
        public void run() {
            try {
                //Wait for new connections from all peers after itself.
                ServerSocket server = new ServerSocket(currentNode.getportNumber());
                byte[] handshakePacket = new byte[32];
                boolean newPeers = false;
                for(Integer peerID : peerMap.keySet()){
                    ConnectedPeerNode tempPeer = peerMap.get(peerID);
                    if(newPeers){
                        System.out.println("Establishing connection  ..... ");
                        Socket socket = server.accept();
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        System.out.println("Read the conn, inps data");
                        inputStream.readFully(handshakePacket);
                        String receivedHeader = GlobalHelperFunctions.extractString(handshakePacket, 0, 17);
                        String receivedPeerId = GlobalHelperFunctions.extractString(handshakePacket, 28, 31);

                        System.out.println("Handshake msg on server side : " + GlobalHelperFunctions.extractString(handshakePacket, 0, 31));
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        handshakePacket = GlobalHelperFunctions.generateHandshakePacket(currentNodeId);
                        outputStream.write(handshakePacket);
                        outputStream.flush();

                        connectionsMap.put(peerID, new AdjacentConnectionNode(socket, peerID));
                        logger.logInfo("Peer" + currentNodeId + " is connected from Peer" + peerID);
                    }

                    if (currentNodeId == peerID) newPeers = true;
                }
                //Completes after all TCP connections have been created.
                server.close();

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

    }


    private static class ClientThreadRunnable implements Runnable {

        @Override
        public void run() {
            try {
                for (Integer neighborPeerId : peerMap.keySet()) {
                    if (neighborPeerId == currentNodeId) break;

                    System.out.println("Client: spawned for " + neighborPeerId);

                    ConnectedPeerNode neighborPeer = peerMap.get(neighborPeerId);
                    Socket socket = new Socket(neighborPeer.getNameOfHost(), neighborPeer.getportNumber());
                    logger.logInfo("Peer " + currentNodeId + " makes a connection to Peer "+neighborPeerId);
                    System.out.println("Client: " + neighborPeerId + " Socket created. Connecting to Server: " + neighborPeer.getNameOfHost()
                            + " with " + neighborPeer.getportNumber());
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    byte[] handshakePacket = GlobalHelperFunctions.generateHandshakePacket(currentNodeId);
                    outputStream.write(handshakePacket);
                    outputStream.flush();

                    System.out.println("Client: " + neighborPeerId + " Handshake packet sent to Connecting to Server: "
                            + neighborPeer.getNameOfHost() + " with " + neighborPeer.getportNumber());

                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    inputStream.readFully(handshakePacket);
                    String receivedHeader = GlobalHelperFunctions.extractString(handshakePacket, 0, 17);
                    String receivedPeerId = GlobalHelperFunctions.extractString(handshakePacket, 28, 31);

                    //Authenticating handshake
                    if(receivedHeader.equals(GlobalConstants.getHandShakeHeader()) && Integer.parseInt(receivedPeerId) == neighborPeerId){

                        connectionsMap.put(neighborPeerId, new AdjacentConnectionNode(socket, neighborPeerId));
                        System.out.println("Client: " + neighborPeerId + " Handshake packet received from Server: " + neighborPeer.getNameOfHost()
                                + " with " + neighborPeer.getportNumber() + " Packet = " + GlobalHelperFunctions.extractString(handshakePacket, 0, 31) + " appended to "
                                + " Updated connections " + connectionsMap.size() + "/" + (peerMap.size() - 1)
                                + " connections till now");
                    }else{
                        socket.close();
                    }
                }

            } catch (IOException exception) {

                exception.printStackTrace();

            } finally {
                System.out.println("donoe & exititng");
            }
        }
    }



    // intializer functions

    public static void initializeResources(String peerId) throws IOException {
        globalMessageTypes = new GlobalMessageTypes();//for messages
        globalConfigReader = new CommonConfigReader();//config file reader
        commonConfigData = new CommonConfigData();//common cfg data
        peerMap = new LinkedHashMap<>();//peer info cfg hash map
        connectionsMap = new ConcurrentHashMap<>();
        createDirectory(peerId);
        getTorrentDetails();
        readCommonConfig();
        commonConfigData.printConfigDetails();
        readPeerConfig();
        logger = new MyLogger(peerId);
        connectionsMap = new ConcurrentHashMap<>();
    }

    public static void readPeerConfig() throws IOException {
        ArrayList<String> aList = parseFile(GlobalConstants.getPeerInfoFileName());
        for(String line : aList){
            String[] words = line.split(" ");
            peerMap.put(Integer.valueOf(words[0]), new ConnectedPeerNode(Integer.parseInt(words[0]), words[1], Integer.valueOf(words[2]), Integer.parseInt(words[3])));
        }
    }

    public static void readCommonConfig() throws IOException {
        ArrayList<String> aList = parseFile(GlobalConstants.getCommonConfigFileName());
        /*if(aList.size() != 6){
            logger.logError("Common.cfg file does not have enough lines. Needs 6 lines.");
        }*/

        commonConfigData.preferredNumberOfNeighbours = Integer.parseInt(aList.get(0).split(" ")[1]);
        commonConfigData.intervalOfUnchoking = Integer.parseInt(aList.get(1).split(" ")[1]);
        commonConfigData.intervalOfOptimisticUnchoking = Integer.parseInt(aList.get(2).split(" ")[1]);
        commonConfigData.myFileName = aList.get(3).split(" ")[1];
        commonConfigData.fileSize = Integer.parseInt(aList.get(4).split(" ")[1]);
        commonConfigData.chunkSize = Integer.parseInt(aList.get(5).split(" ")[1]);
    }

    public static ArrayList<String> parseFile(String fileName) throws IOException {
        ArrayList<String> aList = new ArrayList<>();
        BufferedReader bReader = new BufferedReader(new FileReader(fileName));
        String line = bReader.readLine();
        while(line != null){
            aList.add(line);
            line = bReader.readLine();
        }
        bReader.close();
        return aList;
    }

    public static void createDirectory(String peerId) throws IOException {

        Path path = Paths.get(".//"+peerId);
        //Deleting folder if it exists
        if(Files.exists(path)){
            clearDirectory(path);
            currentNodeDir = path.toFile();
        }else{
            currentNodeDir = Files.createDirectory(path).toFile();
        }
        //Files.createDirectory(path);
    }

    //deletes all files other than torrent file
    private static void clearDirectory(Path path) throws IOException {
        //Deleting every file.
        String fileName = GlobalConstants.getTorrentFileName();
        Stream<Path> files = Files.list(path);
        for(Object obj : files.toArray()){
            Path file = (Path) obj;
            if(!file.getFileName().toString().equals(fileName)){
                Files.deleteIfExists(file);
            }
        }
        files.close();
    }
    // **********
    public static void writeFilePieces() throws IOException {
        String totPath = globalConfigReader.getRootPath()+ currentNodeId + "/" + GlobalConstants.getTorrentFileName();
        BufferedInputStream file = new BufferedInputStream(new FileInputStream(totPath));
        int fileSize = commonConfigData.getFileSize();
        int pieceSize = commonConfigData.getPieceSize();
        byte[] fileBytes = new byte[fileSize];

        // Read the file here.
        file.read(fileBytes);

        // close the file
        file.close();

        int counter = 0, index = 0;
        while(counter < fileSize) {
            if (counter + pieceSize <= fileSize)
                currentFilePieces[index] = GlobalHelperFunctions.copyByteArray(fileBytes, counter, counter + pieceSize);
            else
                currentFilePieces[index] = GlobalHelperFunctions.copyByteArray(fileBytes, counter, fileSize);
            currentNode.updateNumOfPieces();
            counter += pieceSize;
            index++;
        }
    }

    // helper functions for dividing the files
    public static void divideIntoPieces() throws IOException {
        int fileSize = commonConfigData.getFileSize();
        int pieceSize = commonConfigData.getPieceSize();

        int noOfPieces = (int) Math.ceil((double)fileSize / pieceSize);
        currentFilePieces = new byte[noOfPieces][];

        // Will signify number of piece currently received in the file.
        int[] pieceMarker = new int[noOfPieces];
        Arrays.fill(pieceMarker, 1);

        // Check if this peer, already has a file.
        if(currentNode.hasFile()){
            peersWithCompleteFiles++;
            // mark all piece to 1, because we have the entire file.
            Arrays.fill(pieceMarker, 1);
            currentNode.setPieceMarker(pieceMarker);
            // main part remaining
            writeFilePieces();

        }else{
            Arrays.fill(pieceMarker, 0);
            currentNode.setPieceMarker(pieceMarker);
        }
    }

    public static void getTorrentDetails() {
        torFileName = GlobalConstants.getTorrentFileName();
    }
    public static void main(String[] args) throws IOException {

        currentNodeId = Integer.parseInt(args[0]);
        initializeResources(String.valueOf(currentNodeId));        
        currentNode = peerMap.get(currentNodeId);

        // 2. create logs
        log_file = new File(System.getProperty("user.dir") + "/" + "log_peer_" + currentNodeId + ".log");
        if (log_file.exists() == false)
            log_file.createNewFile();


        // 3. divideFilesIntoChunks & Write if the file is completed
        divideIntoPieces();

        // .....
        Thread clientThread = new Thread(new ClientThreadRunnable());
        Thread serverThread = new Thread(new ServerThreadRunnable());
        Thread up = new Thread(new UnchokedPeer());
        Thread oup = new Thread(new OptimistcallyUnchokedPeer());

        clientThread.start();
        serverThread.start();
        up.start();
        oup.start();

    }
    static class ValueComparator implements Comparator<Integer> {

        Map<Integer, Double> base;
        public ValueComparator(Map<Integer, Double> base) {
            this.base = base;
        }

        public int compare(Integer a, Integer b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
  
  private static class UnchokedPeer implements Runnable {
	 
	public List<Integer> getConnectionIDs() {
    List<Integer> connectionIDs = new ArrayList<>();
    for (int i: connectionsMap.keySet()) {
      connectionIDs.add(i);
    }
    return connectionIDs;
  }
  
	
	public List<Integer> getInterestedConnections(List<Integer> connectionsList) {
    List<Integer> interestedPeers = new ArrayList<>();
    int i = 0;
    while(i< connectionsList.size()){
      int connection = connectionsList.get(i);
      AdjacentConnectionNode tempConnection = connectionsMap.get(connection);
      if (tempConnection.isInterested()) {
            interestedPeers.add(connection);
          }
      i++;
    }
    return interestedPeers;
	}

	public List<Integer> getPeers_DownloadRate(List<Integer> connectionsList) {
		List<Integer> interestedPeers = new ArrayList<>();
    for (int peer : connectionsList) {
        AdjacentConnectionNode connectionObject = connectionsMap.get(peer);
        if (connectionObject.isInterested() && connectionObject.getDownloadSpeed() >= 0)
          interestedPeers.add(peer);
      }
    return interestedPeers;
	}
	 
    @Override
    public void run() {
    	
      while (peersWithCompleteFiles < peerMap.size()) {
        List<Integer> connections = getConnectionIDs();

        // check if the file is prsent or not
        if (currentNode.getHasFile() == 1) { // This peer has the file.
          List<Integer> interestedPeers = getInterestedConnections(connections);
          System.out.println("Number of interested peers: "+interestedPeers.size());
          if (interestedPeers.size() <= 0) {
              System.out.println("No interested peers left");
          } 
          else {
        	  // int prefNeighbours = commonConfigData.getPreferredNumberOfNeighbours();
            // int intresLen = interestedPeers.size();
            if (interestedPeers.size() <= commonConfigData.getPreferredNumberOfNeighbours()) {
                // for (int i=0; i < interestedPeers.size(); i++) {
                int i = 0;
                while(i< interestedPeers.size()){
                	int peer = interestedPeers.get(i);
                	AdjacentConnectionNode tempConnection = connectionsMap.get(peer);
                  if (tempConnection.isChoked()==true) {
                    System.out.println("Unchoking preferred neighbours");
                    tempConnection.unChokeConnection();
                    tempConnection.sendUnChokeMessage();
                  }
                  i++;
                }
              } 
              else {
                System.out.println("When you have more prees than cofig");
                // int prefNeigboursSize = commonConfigData.getPreferredNumberOfNeighbours();
                int[] preferredNeighbors = new int[commonConfigData.getPreferredNumberOfNeighbours()];
                Random random = new Random();
                int i = 0;
                // for (int i = 0; i < commonConfigData.getPreferredNumberOfNeighbours(); i++) {
                while(i<commonConfigData.getPreferredNumberOfNeighbours()){
                  int someIndex = Math.abs(random.nextInt() % interestedPeers.size());
                  preferredNeighbors[i] = interestedPeers.remove(someIndex);
                }
                
                // for (int i=0;i < commonConfigData.getPreferredNumberOfNeighbours();i++) {
                i=0;
                while(i<commonConfigData.getPreferredNumberOfNeighbours()){
                  int tempPeerId = preferredNeighbors[i];	
                  AdjacentConnectionNode connectionObject = connectionsMap.get(tempPeerId);
                  if (connectionObject.isChoked() == true) {
                   connectionObject.unChokeConnection();
                   connectionObject.sendUnChokeMessage();
                  }
                  i++;
                }
                i=0;
                // for (int i=0;i < interestedPeers.size();i++) {
                while(i<interestedPeers.size()){
                  int peer = interestedPeers.get(i);
                  AdjacentConnectionNode connectionObject = connectionsMap.get(peer);
                  boolean isChoked = connectionObject.isChoked(), isOpUnchoked = connectionObject.isOptimisticUnchoke();
                  if (isChoked == false && isOpUnchoked == false) { // What the it is
                    System.out.println(peer+ " is choked optimistically");
                    connectionObject.chokeConnection();
                    connectionObject.sendChokeMessage();
                  }
                  i++;
                }
                System.out.println("Preferred neighbours of node" + currentNodeId + " is " + preferredNeighbors);
                logger.logInfo("Preferred neighbours of node" + currentNodeId + " are " + preferredNeighbors);
                // try {
                //    BufferedWriter logger = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
                //    GlobalHelperFunctions.logger_change_preferred_neighbors(logger, currentNode.getPeerId(), preferredNeighbors);
                //    logger.close();
                // } 
                // catch (IOException exception) {
                //   System.out.println("Error: Exception caught");
                //   exception.printStackTrace();
                // }
  
              }
          }
        } 
        else {
          System.out.println("File does not exist in this peer");
          List<Integer> interestedPeers = getPeers_DownloadRate(connections);
          // int prefNeigboursSize = commonConfigData.getPreferredNumberOfNeighbours();
          if (interestedPeers.size() <= commonConfigData.getPreferredNumberOfNeighbours()) {
            int i=0;
            while(i < interestedPeers.size()) {
        	    int peer = interestedPeers.get(i);
              AdjacentConnectionNode connectionObject = connectionsMap.get(peer);
              if (connectionObject.isChoked() == false) {
                // do nothing...
              } 
              else {
                System.out.println("Sending chunks of existing file");
                connectionObject.unChokeConnection();
                connectionObject.sendUnChokeMessage();
              }
              i++;
            }
          } 
          else {
            int[] preferredNeighbors = new int[commonConfigData.getPreferredNumberOfNeighbours()];
            // System.out.println("Jab prefs zyada ho..WITHOUT FILE PEER HO");
            HashMap<Integer,Double> map = new HashMap<Integer,Double>();
            ValueComparator comp =  new ValueComparator(map); /***************************************************************************************************/
            TreeMap<Integer,Double> sortedMap = new TreeMap<Integer,Double>(comp);
            for(int i=0; i<interestedPeers.size(); i++){
              map.put(interestedPeers.get(i),connectionsMap.get(interestedPeers.get(i)).getDownloadSpeed());
            }
            sortedMap.putAll(map);
            List<Integer> sortedPeers = new ArrayList<Integer>();
	          sortedPeers.addAll(sortedMap.keySet());
            // for(int j=0; j < commonConfigData.getPreferredNumberOfNeighbours();j++){
            int i = 0;
            while(i<commonConfigData.getPreferredNumberOfNeighbours()){
              int peer= sortedPeers.get(i);
              preferredNeighbors[i]= peer;
              AdjacentConnectionNode tempConnection = connectionsMap.get(peer); 
              if(tempConnection.isChoked()){
            	 tempConnection.unChokeConnection();
            	 tempConnection.sendUnChokeMessage();
              } 
              interestedPeers.remove(peer);
              i++;
            }
            for(int j=0;j<interestedPeers.size();j++){
              int peer = interestedPeers.get(j);
              AdjacentConnectionNode tempConnection = connectionsMap.get(peer);
              if( tempConnection.isChoked() == false && tempConnection.isOptimisticUnchoke() == false){
            	  tempConnection.chokeConnection();
            	  tempConnection.sendChokeMessage();
              }
            }
            System.out.println("Preferred neighbours of node" + currentNodeId + " are " + preferredNeighbors);
            logger.logInfo("Preferred neighbours of node" + currentNodeId + " are " + preferredNeighbors);
          }

        }
        try {
          System.out.println("Thread sleeps....");
          // int unchokeTime = commonConfigData.getUnchokingInterval();
          // int unChokeTimeInSecs = unchokeTime * 1000;
          Thread.sleep(commonConfigData.getUnchokingInterval()*1000);
        } 
        catch (Exception exception) {

           exception.printStackTrace();
        } 
        finally {
          System.out.println("Inner thread has finished executing");
        } 
      }
      try {
    	  
        // Thread.sleep(5000);
        
      } 
      catch (Exception exception) {
        exception.printStackTrace();
      } 
      finally {
        System.out.println("Outer thread has finished executing");
      }
      System.exit(0);
    }
  }

  private static class OptimistcallyUnchokedPeer implements Runnable { //OUPRunnable changed to OptimistcallyUnchoked
	 
	public List<Integer> getConnectionIDs() { // DONE
        List<Integer> connectionIDs = new ArrayList<>();
        for (int i: connectionsMap.keySet()) {
        	connectionIDs.add(i);
        }
        return connectionIDs;
  }
  
	public List<Integer> getInterestedConnections(List<Integer> connectionsList) { // DONE
        List<Integer> interestedPeers = new ArrayList<>();
        int i = 0;
        while(i< connectionsList.size()){
          int connection = connectionsList.get(i);
          AdjacentConnectionNode tempConnection = connectionsMap.get(connection);
          if (tempConnection.isInterested()) {
                interestedPeers.add(connection);
              }
          i++;
        }
        return interestedPeers;
	}
    @Override
    public void run() {  // DONE
      while (peersWithCompleteFiles < peerMap.size()) {
    	
    	// List<Integer> connections = getConnectionIDs();
    	  List<Integer> interestedPeers = getInterestedConnections(getConnectionIDs());
        if (interestedPeers.size() > 0) {
          Random random = new Random();
          // int intLen = interestedPeers.size();
          int someIndex = Math.abs(random.nextInt() % interestedPeers.size());
          // int randomConnId = interestedPeers.get(someIndex);
          
          AdjacentConnectionNode getConnection = connectionsMap.get(interestedPeers.get(someIndex));
          getConnection.unChokeConnection();
          getConnection.sendUnChokeMessage();
          getConnection.optimisticallyUnchoke();
          logger.logInfo("Changed Optimistically unchoked neighbour " + currentNodeId + " : " + getConnection.getPeerId());

          try {
            // Sleeping the thread
            Thread.sleep(commonConfigData.getOptimisticUnchokingInterval() * 1000);
            getConnection.optimisticallyChoke();  // Choke this connection as well.

          } 
          catch (Exception exception) {
            System.out.println("Error");
            // exception.printStackTrace();
          } 
          finally {
            System.out.println("Fin");
          }
        }
      }
      try {
        System.out.println("Thread is sleeping Optimistic");
        Thread.sleep(5000);
      } 
      catch (Exception e) {
        System.out.println("Error");
      }
      System.exit(0);
    }
  }
}


class WrongPacketException extends Exception {
	public WrongPacketException(String s) {
		super(s);
	}
}