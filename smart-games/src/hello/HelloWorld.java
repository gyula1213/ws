package hello;

public class HelloWorld {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world!");
		for ( int i=0; i<6; i++ )
		{
			for ( int j=0; j<16; j++ )
			{
				System.out.print( 16*i + j + ", ");
			}
			System.out.println();
		}

	}

}
