//********************************************************************
// Led.java  
//       Egy szamjegyekbol, ':', '-' jelekbol allo string
//       'LED'-szeru kijelzeset teszi lehetove
//********************************************************************

package demo;

import java.awt.Graphics;

//********************************************************************

public class Led
  {
    private Graphics g;

    private int x_points[];
    private int y_points[];

    private int dx = 5;
    private int dy = 10;

    private int digits[][] =
      {
        {1, 1, 1, 1, 1, 1, 0},     // 0
        {0, 0, 0, 0, 1, 1, 0},     // 1
        {1, 0, 1, 1, 0, 1, 1},     // 2
        {1, 0, 0, 1, 1, 1, 1},     // 3
        {0, 1, 0, 0, 1, 1, 1},     // 4
        {1, 1, 0, 1, 1, 0, 1},     // 5
        {1, 1, 1, 1, 1, 0, 1},     // 6
        {1, 0, 0, 0, 1, 1, 0},     // 7
        {1, 1, 1, 1, 1, 1, 1},     // 8
        {1, 1, 0, 1, 1, 1, 1},     // 9
      };

    //----------------------------------------------------------------

    public Led()
      {
        x_points = new int[6];
        y_points = new int[6];
      }

    //----------------------------------------------------------------

    public void drawString( Graphics g, String string, int x, int y, int w, int h)
      {
        if ( string.equals("") )
            return;
            
        int string_length = string.length();
        char char_array[] = new char[string_length];
        string.getChars(0, string_length, char_array, 0);

        this.g = g;

        w /= string_length;

        int char_width  = (w * 7) / 8;
        int char_height = (h * 7) / 8;

        for (int i = 0; i < string_length; i++)
          {
            drawChar(char_array[i], x, y, char_width, char_height);
            x += w;
          }
      }

    //----------------------------------------------------------------

    private void drawChar(char c, int x, int y, int w, int h)
      {
        switch (c)
          {
            case ':':
              g.fillRect(x+2*w/dx, y-(h  )/4, w/dx, h/dy);
              g.fillRect(x+2*w/dx, y-(h*3)/4, w/dx, h/dy);
              break;

            case '-':
	      middle (x  , y-h/2, w   , h/dy);
              break;
            
            case ' ':
                break;

            default:
              int index = Character.digit(c, 10);
              for (int i = 0; i < 7; i++)
                if (digits[index][i] == 1)
                  switch (i)
                    {
                      case 0:  top    (x  , y-h  , w   , h/dy); break;
                      case 1:  left   (x  , y-h/2, w/dx, h/ 2); break;
                      case 2:  left   (x  , y    , w/dx, h/ 2); break;
                      case 3:  bottom (x  , y    , w   , h/dy); break;
                      case 4:  right  (x+w, y    , w/dx, h/ 2); break;
                      case 5:  right  (x+w, y-h/2, w/dx, h/ 2); break;
                      case 6:  middle (x  , y-h/2, w   , h/dy); break;
                    }
              break;
          }
      }

    //----------------------------------------------------------------

    private void top(int x, int y, int w, int h)
      {
        x_points[0] = x        ;  y_points[0] = y  ;
        x_points[1] = x+w      ;  y_points[1] = y  ;
        x_points[2] = x+(w*7)/8;  y_points[2] = y+h;
        x_points[3] = x+(w  )/8;  y_points[3] = y+h;

        g.fillPolygon(x_points, y_points, 4);
      }

    //----------------------------------------------------------------

    private void middle(int x, int y, int w, int h)
      {
        x_points[0] = x          ;  y_points[0] = y    ;
        x_points[1] = x+(w  )/8  ;  y_points[1] = y-h/2;
        x_points[2] = x+(w*7)/8  ;  y_points[2] = y-h/2;
        x_points[3] = x+w        ;  y_points[3] = y    ;
        x_points[4] = x+(w*7)/8  ;  y_points[4] = y+h/2;
        x_points[5] = x+(w  )/8  ;  y_points[5] = y+h/2;

        g.fillPolygon(x_points, y_points, 6);
      }

    //----------------------------------------------------------------

    private void bottom(int x, int y, int w, int h)
      {
        x_points[0] = x        ;  y_points[0] = y  ;
        x_points[1] = x+w      ;  y_points[1] = y  ;
        x_points[2] = x+(w*7)/8;  y_points[2] = y-h;
        x_points[3] = x+(w  )/8;  y_points[3] = y-h;

        g.fillPolygon(x_points, y_points, 4);
      }

    //----------------------------------------------------------------

    private void left(int x, int y, int w, int h)
      {
        x_points[0] = x  ;  y_points[0] = y        ;
        x_points[1] = x  ;  y_points[1] = y-h      ;
        x_points[2] = x+w;  y_points[2] = y-(h*7)/8;
        x_points[3] = x+w;  y_points[3] = y-(h  )/8;

        g.fillPolygon(x_points, y_points, 4);
      }

    //----------------------------------------------------------------

    private void right(int x, int y, int w, int h)
      {
        x_points[0] = x  ;  y_points[0] = y        ;
        x_points[1] = x  ;  y_points[1] = y-h      ;
        x_points[2] = x-w;  y_points[2] = y-(h*7)/8;
        x_points[3] = x-w;  y_points[3] = y-(h  )/8;

        g.fillPolygon(x_points, y_points, 4);
      }
  }
