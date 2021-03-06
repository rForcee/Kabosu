package objectIhm;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Meteo {
	
	private Image skin;
	private int meteo;
	public Image getIcoMeteo()
	{
		switch (this.meteo){
    	case 1:
    		setSkin(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/canicule.png"));
    		return skin;
    	case 2:
    		setSkin(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/soleil.png"));
    		return skin;
    	case 3:
    		setSkin(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/nuage.png"));
    		return skin;
    	case 4:
    		setSkin(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/pluie.png"));
    		return skin;
    	case 5:
    		setSkin(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/orage.png"));
    		return skin;
    		
		}
		return skin;
	}
	public void afficheMeteo(Group root,Scene scene){
		Rectangle rectangle = new Rectangle();
		rectangle.setX(scene.getWidth()-75);
		rectangle.setY(0);
		rectangle.setWidth(75);
		rectangle.setHeight(75);
		rectangle.setFill(new ImagePattern(getIcoMeteo()));
		root.getChildren().add(rectangle);
	}
	public int getMeteo() {
		return meteo;
	}

	public void setMeteo(int meteo) {
		this.meteo = meteo;
	}
	
	public void setSkin(Image skin) {
		this.skin = skin;
	}

}
