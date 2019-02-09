/*
 * Created on 21-Jul-2005
 */
package pipe.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import pipe.calculations.MarkingState;

/**
 * @author Nadeem
 * @author Matthew Worthington/Edwin Chung - modifications made both to the read
 * and write methods to enable reachability call to generate method in
 * StateSpaceGenerator to save type of state - i.e. tangible or not. Also added
 * new helper methods. This class is used to record states in a file.
 */
public class StateRecord {

    private int stateid;
    private int[] state = null;
    private boolean isTangible;

    public StateRecord() {
    }

    public StateRecord(MarkingState newstate) {
        stateid = newstate.getIDNum();
        state = new int[newstate.getState().length];
        System.arraycopy(
                newstate.getState(), 0, state, 0, newstate.getState().length);
        isTangible = newstate.getIsTangible();
    }

    public StateRecord(int stateid, boolean isTangible, int[] state) {
        this.stateid = stateid;
        this.isTangible = isTangible;
        this.state = state;
    }

    public StateRecord(StateRecord s) {
        stateid = s.stateid;
        state = new int[s.state.length];
        state = s.state;
        isTangible = s.isTangible;
    }

    public void write(RandomAccessFile opfile) throws IOException {
        if (state == null) {
            return;
        }
        opfile.writeInt(stateid);
        for (int aState : state) {
            opfile.writeInt(aState);
        }
    }

    public void write(RandomAccessFile opfile, boolean Tangible)
            throws IOException {
        write(opfile);
        opfile.writeBoolean(Tangible);
    }

    public void read(int statesize, RandomAccessFile ipfile) throws IOException {
        state = new int[statesize];
        stateid = ipfile.readInt();
        for (int index = 0; index < state.length; index++) {
            state[index] = ipfile.readInt();
        }
    }

    public void read1(int statesize, RandomAccessFile ipfile) throws IOException {
        read(statesize, ipfile);
        isTangible = ipfile.readBoolean();
    }

    public int[] getState() {
        return state;
    }

    public int getID() {
        return stateid;
    }

    public boolean getTangible() {
        return isTangible;
    }

    public String toString() {
        String s = String.valueOf(stateid) + " - ";

        for (int aState : state) {
            s += aState;
        }

        return s + " [tangible? " + isTangible + "]\n";
    }

    public String getMarkingString() {
        String s = "{";

        for (int i = 0; i < state.length - 1; i++) {
            if (state[i] == -1) {
                s += "\u03C9, "; //\u03C9: Unicode Character 'GREEK SMALL LETTER OMEGA'
            } else {
                s += state[i] + ", ";
            }
        }
        if (state[state.length - 1] == -1) {
            s += "\u03C9";
        } else {
            s += state[state.length - 1];
        }
        s += "}";

        return s;
    }

    // for checking if this state is investigated to state
    // investigate mean (this state[i] investigte state[i]
    // if (X = any) or (x <= y) then this state is investigated
    // -2 is mean X 
    //  X is mean the number of tokens in the place in not important
    public boolean IsInvestigate(StateRecord state) {

        if (this != null && state != null) {
            if (this.getState().length != state.getState().length) {
                return false;
            } else {
                int size = this.getState().length;
                for (int i = 0; i < this.getState().length; i++) {
                    // -2 means not nesessary
                    if (this.getState()[i] == -2 || state.getState()[i] == -2) {
                        continue;
                    }
                    // 0 means empty
                    if ((this.getState()[i] == 0) && (state.getState()[i] == 0)) {
                        continue;
                    }
                    if ((this.getState()[i] == 0) && (state.getState()[i] != 0)) {
                        return false;
                    }
                    if ((this.getState()[i] != 0) && (state.getState()[i] == 0)) {
                        return false;
                    }
                    // -3 means has token
                    if ((this.getState()[i] == -3) && (state.getState()[i] != 0)) {
                        continue;
                    }
                    if ((this.getState()[i] != 0) && (state.getState()[i] == -3)) {
                        continue;
                    }
                    if ((this.getState()[i] == -3) && (state.getState()[i] == 0)) {
                        return false;
                    }
                    if ((this.getState()[i] == 0) && (state.getState()[i] == -3)) {
                        return false;
                    }
                    if ((state.getState()[i] == this.getState()[i])) {
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    // Our code for garduated Project 
    // here just return the state and it's id that we read

    public int[] ReadandGetVector(int statesize, RandomAccessFile ipfile) throws IOException {
        state = new int[statesize];
        for (int index = 0; index < state.length; index++) {
            state[index] = ipfile.readInt();
        }
        isTangible = ipfile.readBoolean();
        return state;
    }

    public int ReadandGetID(int statesize, RandomAccessFile ipfile) throws IOException {
        stateid = ipfile.readInt();
        return stateid;
    }

}
