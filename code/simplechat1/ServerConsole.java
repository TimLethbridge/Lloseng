import java.io.*;
import common.*;
public class ServerConsole implements ChatIF{
	EchoServer serv;
	final public static int DEFAULT_PORT = 5555;
	public ServerConsole(int port, EchoServer server){
		this.serv=server;
		try{
			this.accept();
		}catch(Exception e){e.printStackTrace();System.exit(1);}
		
		
	}

	public void display(String message){
		System.out.println("> "+message);
	}	

	public void accept(){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String message;
			while(true){
				message=input.readLine();
				this.checkComs(message);
				serv.handleMessageFromConsole(message);
			}
		} catch(Exception e){
			System.out.println("Unexpected error while reading from console!");
		}
	}
	public void checkComs(String msg){
		try{
			if(msg.charAt(0)=='#'){
		      if(msg.equals("#quit")){
		        serv.close();
		        System.exit(0);
		      }else if (msg.equals("#stop")) {
		        serv.stopListening();
		      }else if (msg.equals("#close")) {
		        serv.close();
		      }else if (msg.equals("#getport")){
		        this.display(Integer.toString(serv.getPort()));
		      }else if (msg.equals("#start")&&!serv.isListening()){
		        serv.listen();
		      }else if (msg.substring(0,8).equals("#setport")&&!serv.isListening()) {
		        serv.setPort(Integer.parseInt(msg.substring(9)));
		      }
	    	}
		}catch(Exception e){}
		
	}
	// public static void main(String[] args){

	// 	String host="";
	//     int port=0; 
	//     try
	//     {
	//       host=args[0];
	//       port=Integer.parseInt(args[1]);
	//     }
	//     catch(ArrayIndexOutOfBoundsException e)
	//     {
	//       host="localhost";
	//       port=DEFAULT_PORT;
	//     }

	//     //EchoServer server = new EchoServer(port);

	// 	ServerConsole console = new ServerConsole(host, port);

	// 	//--BREAK POINT HERE--
	// 	// cons=new Serverconsole(port);
	// 	// cons.accept();
	// }
}