package map;
import map.Position;
import map.Spand;
public class Map 
{
	
	
	public Position centre ;
	public Spand pointSpand ;

	public Map(float X,float Y,float Sx,float Sy){
		
		this.centre = new Position(X,Y);
		this.pointSpand = new Spand(Sx,Sy);
	}
	

	public Double calculeRayon(float X,float Y,float Sx,float Sy){
		double tmpCal = ((Y-X)*(Y-X))+((Sy-Sx)*(Sy-Sx));
		System.out.println("tmp: "+tmpCal);
		 return Math.sqrt(Math.abs(tmpCal));
	}
	
}