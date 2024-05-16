package com.os.frontend.Colors;

import javafx.scene.paint.Color;

public class Colors {
    private static final Color[] colors = {
            Color.RED,
            Color.ORANGE,
            Color.LIGHTBLUE,
            Color.YELLOW,
            Color.color(0.85, 0.64, 0.58),
            Color.PURPLE,
            Color.CYAN,
            Color.MAGENTA,
            Color.PINK,
            Color.LIME,
            Color.BROWN,
            Color.GRAY,
            Color.BLUE,
            Color.LIGHTGREEN,
            Color.LIGHTPINK,
            Color.LIGHTGRAY,
            Color.DARKRED,
            Color.DARKGREEN,
            Color.DARKBLUE,
            Color.DARKORANGE,
            Color.AQUA,
            Color.BLACK,
            Color.WHITE,
            Color.SILVER,
            Color.GOLD,
            Color.INDIGO,
            Color.TEAL,
            Color.VIOLET,
            Color.YELLOWGREEN,
            Color.CRIMSON,
            Color.LAVENDER,
            Color.SALMON,
            Color.CHOCOLATE,
            Color.TURQUOISE,
            Color.CORAL,
            Color.LIGHTCORAL,
            Color.DARKCYAN,
            Color.LIGHTCYAN,
            Color.DARKVIOLET,
            Color.MEDIUMVIOLETRED,
            Color.LIGHTSALMON,
            Color.LIGHTSTEELBLUE,
            Color.DARKGOLDENROD,
            Color.DARKKHAKI,
            Color.LIGHTSEAGREEN,
            Color.DARKSLATEGRAY,        
            Color.DEEPPINK,            
            Color.FORESTGREEN,          
            Color.HOTPINK,              
            Color.MEDIUMBLUE,           
            Color.OLIVE,               
            Color.PLUM,                 
            Color.ROYALBLUE,            
            Color.SIENNA,               
            Color.SLATEGRAY,            
            Color.TOMATO,               
            Color.WHEAT,                
            Color.DARKOLIVEGREEN,       
            Color.DARKORCHID,           
            Color.DARKSLATEBLUE,        
            Color.DIMGRAY,              
            Color.LIGHTGOLDENRODYELLOW, 
            Color.LIGHTSLATEGRAY,       
            Color.MEDIUMAQUAMARINE,     
            Color.MEDIUMSEAGREEN,       
            Color.MEDIUMSLATEBLUE,      
            Color.MEDIUMSPRINGGREEN,    
            Color.MEDIUMTURQUOISE,      
            Color.MEDIUMVIOLETRED,      
            Color.MIDNIGHTBLUE,         
            Color.OLIVEDRAB,            
            Color.ORANGERED,            
            Color.ORCHID,               
            Color.PALEGOLDENROD,        
            Color.PALEGREEN,            
            Color.PALETURQUOISE,        
            Color.PALEVIOLETRED,        
            Color.PAPAYAWHIP,           
            Color.PEACHPUFF,            
            Color.PERU,                 
            Color.POWDERBLUE,           
            Color.ROSYBROWN,            
            Color.SADDLEBROWN,          
            Color.SANDYBROWN,           
            Color.SEAGREEN,             
            Color.SEASHELL,           
            Color.SKYBLUE,              
            Color.SLATEBLUE,            
            Color.SNOW,                 
            Color.SPRINGGREEN,          
            Color.STEELBLUE,            
            Color.TAN,                  
            Color.THISTLE,              
            Color.TOMATO,               
            Color.TURQUOISE,            
            Color.VIOLET,               
            Color.WHEAT,                
            Color.WHITESMOKE,           
            Color.YELLOWGREEN,          
            Color.DARKGRAY               

    };

    public static String getColor(int index) {
        if (index >= 0 && index < colors.length) {
            Color color = colors[index];
            return toRGBCode(color);
        }
        return null;
    }



    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}


