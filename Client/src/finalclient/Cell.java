/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalclient;

/**
 *
 * @author Dao Van Do
 */
public class Cell 
{

    private char ch;
    public Cell(char c)
    {

        ch = c;  
    }
    public Cell(){};
    char getCh() 
    {
        return ch; 
    }
    
    //  category definition
    void setPosition( char c)
    {

        ch = c;
    }
}
