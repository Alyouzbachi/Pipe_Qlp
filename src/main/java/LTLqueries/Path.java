/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LTLqueries;

import abbot.script.Assert;
import java.util.ArrayList;
import java.util.Stack;
import pipe.io.StateRecord;
import pipe.io.TransitionRecord;

/**
 *
 * @author TOSHIBA
 */
public class Path {

    private Stack<StateRecord> states = new Stack<StateRecord>();

    public Path() {
    }

    public Stack<StateRecord> getStates() {
        return states;
    }

    public Path(Path path) {

        states.addAll(path.getStates());
    }

    public void push(StateRecord state) {

        if (state != null && states != null) {
            states.push(state);
        }
    }

    public boolean contains(StateRecord state) {

        return states.contains(state);

    }

    public ArrayList<StateRecord> getRepeatedStates() {
        ArrayList<StateRecord> RepeatedStates = new ArrayList<StateRecord>();

        for (StateRecord s1 : this.states) {
            if (states.indexOf(s1) != states.lastIndexOf(s1)) {
                if (!RepeatedStates.contains(s1)) {
                    RepeatedStates.add(s1);
                }
            }
        }
        return RepeatedStates;
    }

    public boolean IsContained(ArrayList<Path> array) {

        for (Path path : array) {
            if (this.equals(path)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Path path) {

        return this.states.equals(path.getStates());
    }

    public void pop() {
        states.pop();
    }

    public boolean IssubPathOf(Path path) {
        for (int i = 0; i < this.states.size(); i++) {
            if (path.getStates().get(i) == this.getStates().get(i)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public StateRecord peek() {

        return states.peek();

    }

}
