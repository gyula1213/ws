package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread
	{
		final DataInputStream ournewDataInputstream;
		final DataOutputStream ournewDataOutputstream;
		final Socket mynewSocket;
		private Game game;

		// Constructor
		public ClientHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream)
		{
			this.mynewSocket = mynewSocket;
			this.ournewDataInputstream = ournewDataInputstream;
			this.ournewDataOutputstream = ournewDataOutputstream;
		}
		
		public void startGame() {
			try {
				ournewDataOutputstream.writeUTF("Start");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void addDrink(Drinks drink) {
			try {
				ournewDataOutputstream.writeUTF(drink.getCsv());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void actualCharacter(Character character) {
			try {
				ournewDataOutputstream.writeUTF(character.getCsv());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void setUnderPunishment() {
			try {
				ournewDataOutputstream.writeUTF("Punishment");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setGame(Game game) {
			this.game = game;
		}

		private void createDrink( String s ) {
			String [] f = s.split(";");
			int rnd = Integer.parseInt(f[1]);
			int startPos = Integer.parseInt(f[2]);
			int speed = Integer.parseInt(f[3]);
			Drinks drink = new Drinks(rnd, startPos, speed);
			game.addDrink(drink);
		}
		private void actualOtherCharakter( String s ) {
			String [] f = s.split(";");
			String name = f[1];
			int pos = Integer.parseInt(f[2]);
			int score = Integer.parseInt(f[3]);
			int drunkScore = Integer.parseInt(f[4]);
			int maybePunishment = Integer.parseInt(f[5]);
			game.actualOtherCharakter(name, pos, score, drunkScore, maybePunishment);
		}
		
		@Override
		public void run()
		{
			String receivedString;
			while (true)
			{
				try {
					// getting answers from client
					receivedString = ournewDataInputstream.readUTF();
					//receivedString = ournewDataInputstream.readLine();
					System.out.println("Input: " + receivedString);
					
					if(receivedString.equals("Exit"))
					{
						System.out.println("Client " + this.mynewSocket + " sends exit...");
						System.out.println("Connection closing...");
						this.mynewSocket.close();
						System.out.println("Closed");
						break;
					}
					else if(receivedString.equals("Start"))
					{
						game.gameStart();
					}
					else if(receivedString.startsWith("Drink"))
					{
						createDrink(receivedString);
					}
					else if(receivedString.startsWith("Character"))
					{
						actualOtherCharakter(receivedString);
					}
					else if(receivedString.startsWith("Punishment"))
					{
						game.setPunishment();
					}
				} catch (IOException e) {
					System.out.println( "Connection lost");
					break;
				}
			}
			
			try
			{
				// closing resources
				this.ournewDataInputstream.close();
				this.ournewDataOutputstream.close();
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
