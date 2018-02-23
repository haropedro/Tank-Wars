/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author Jeffrey
 */
public class ThreadID {
    private int ID;
    
    public ThreadID(int id) {
        this.ID = id;
    }
    
    public int next() {
        return ID++;
    }
}
