package media;

import java.io.File;

import saint.media.SimplePlayer;
/**
 * ���ֲ�����
 * @author СY
 *
 */
public class Player {
	
	private static SimplePlayer musicPlayer = new SimplePlayer();
	private static SimplePlayer soundPlayer = null;
	
	
	/**
	 * ����MP3����
	 * @param .mp3�ļ����ļ���
	 */
	public static void playMusic(String name) {
		
	         
		try{
			musicPlayer.open(new File("media/music/" + name + ".mp3"));
			
			musicPlayer.setLoop(true);
			musicPlayer.setLoopCount(1000);
		}catch (Exception e) {
			System.err.println("Error��");
			return;
		}
		try {
			musicPlayer.play();	
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����MP3��Ч
	 * @param .mp3�ļ����ļ���
	 */
	public static void playSound(String name) {
		soundPlayer = new SimplePlayer();
		try {
			soundPlayer.open(new File("media/sound/" + name + ".mp3"));
			soundPlayer.setLoop(false);
		} catch (Exception e) {
			System.err.println("Error!");
			return;
		}
		try {
			soundPlayer.play();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ֹͣmp3����
	 */
	public static void stopMusic() {
//		musicPlayer.setVolume(0);
//	musicPlayer.setVolume(musicPlayer.getVolume()/2);
			
		musicPlayer.stop();
	}


	
}