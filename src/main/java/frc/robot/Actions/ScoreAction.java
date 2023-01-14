package frc.robot.Actions;
import frc.robot.*;

/*
==================== SCORE ACTION ====================

Score action will place a piece in the specified scoring zone
Steps:
    1 - Locate the scoring zone relative to the bot
    2 - Auto align to the scoring zone
    3 - Score

*/

public class ScoreAction implements AutoAction {

    enum State {
        Align,
        Score
    }

    State ActionState;

    public boolean Act(int[] Options) {

        switch (ActionState) {
            case Align:
                //AutoAlign
                break;

            case Score:
                //Score
                //if (Scored)
                    //return true;
                break;
        }

        return false;
    }
}
