package demo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class LedCanvas extends Canvas
{
    private Led led;
    private String string;
    private int border;
    private int digits;
    
        /*
    public LedCanvas( int height )
    {
	    initLedCanvas( height, 1, 0 );
    }
        /* */
    
    public LedCanvas( int height, int digits, int initValue )
    {
	    initLedCanvas( height, digits, initValue );
    }
    
    public void initLedCanvas( int height, int digits, int initValue )
    {
        led = new Led();
        
        setSize( digits * height * 3 / 4, height );
        setBackground(Color.red);
        border = height/30;
        this.digits = digits;
        //string = "0";
        string = String.valueOf(initValue);
    }
     
    public void drawNumber( int value )
    {
        drawNumber( value, digits );
    }
    
    public void drawNumber( int value, int digits )
    {
        this.digits = digits;	//atallitja a default erteket

        string = String.valueOf(value);
        for ( int i = string.length(); i < digits; i++ )
        {
            string = "00" + string;
        }
        repaint();
    }
    
    public void drawClock( int value )
    {
        int hour = value / 3600;
        int min = value % 3600 / 60;
        int sec = value % 60;
        
        String strHour, strMin, strSec;
        
        strHour = String.valueOf(hour);
        strMin = String.valueOf(min);
        strSec = String.valueOf(sec);
        if ( sec == 0 )
            strSec = "0";
        else if ( sec < 10 )
            strSec = "0" + String.valueOf(sec);
        else
            strSec = String.valueOf(sec);
        
            /* !!! atalakitva
        for ( int i = strSec.length(); i < 2; i++ )
        {
            strSec = "0" + strSec;
        }
        /* eddig */
        if ( hour > 0 )
        {
            for ( int i = strMin.length(); i < 2; i++ )
            {
                strMin = "0" + strMin;
            }
            string = strHour + ":" + strMin + ":" + strSec;
        }
        else
        {
            if ( min > 0 )
                string = strMin + ":" + strSec;
            else
                string = "   " + strSec;
        }
            
        repaint();
    }
    
    public void paint( Graphics g )
    {
        led.drawString( g, string, border, getSize().height-border, 
        	getSize().width-2*border, getSize().height-2*border );
    }
}
