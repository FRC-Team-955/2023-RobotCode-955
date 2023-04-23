package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    private static PIDController aprilTagAlignXPID = new PIDController(Constants.AutoAlign.aprilTagAlignXkP, Constants.AutoAlign.aprilTagAlignXkI, Constants.AutoAlign.aprilTagAlignXkD); 
    private static PIDController limelightAlignXPID = new PIDController(Constants.AutoAlign.limelightAlignXkP, Constants.AutoAlign.limelightAlignXkI, Constants.AutoAlign.limelightAlignXkD);
    private static PIDController gamePieceAutoAlignXPID = new PIDController(Constants.AutoAlign.gamePieceAutoAlignXkP, Constants.AutoAlign.gamePieceAutoAlignXkI, Constants.AutoAlign.gamePieceAutoAlignXkD);
    private static PIDController gamePieceTelopAlignXPID = new PIDController(Constants.AutoAlign.gamePieceTelopAlignXkP, Constants.AutoAlign.gamePieceTelopAlignXkI, Constants.AutoAlign.gamePieceTelopAlignXkD);

    private static PIDController odometryAlignXPID = new PIDController(Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignXkI, Constants.AutoAlign.odometryAlignXkD);
    private static PIDController odometryAlignYPID = new PIDController(Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.odometryAlignYkI, Constants.AutoAlign.odometryAlignYkD);
    private static PIDController translationAlignXPID = new PIDController(Constants.AutoAlign.translationAlignXkP, Constants.AutoAlign.translationAlignXkI, Constants.AutoAlign.translationAlignXkD);
    private static PIDController translationAlignYPID = new PIDController(Constants.AutoAlign.translationAlignYkP, Constants.AutoAlign.translationAlignYkI, Constants.AutoAlign.translationAlignYkD);


    Drivebase drive = new Drivebase();

    public static boolean alignOdometrykP(Translation2d goalTranslation, double heading, double XkP, double YkP, double alignTolerance, boolean isOpenLoopDrive){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        odometryAlignXPID.setP(XkP);
        odometryAlignYPID.setP(YkP);
        double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeHeading(translation, heading , isOpenLoopDrive);

        return (Math.abs(goalPoseX - poseX) < alignTolerance && Math.abs(goalPoseY - poseY) < alignTolerance);
     
    }
    public static boolean alignOdometry(Translation2d goalTranslation, double heading){
        return alignOdometrykP(goalTranslation, heading, Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.alignTolerance, true);
    }
    public static boolean alignTranslation(Translation2d goalTranslation){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        double movementX = translationAlignXPID.calculate(poseX, goalPoseX);
        double movementY = translationAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeRotation(translation, IO.Drivebase.getSwerveRotation() *0.05, true, true);

        return (Math.abs(goalPoseX - poseX) < Constants.AutoAlign.alignTranslationX && Math.abs(goalPoseY - poseY) < Constants.AutoAlign.alignTranslationX);
    }

    public static boolean alignAprilTag(){
        if(AprilTagCameraWrapper.hasTargets()){
            double movementY = aprilTagAlignXPID.calculate(AprilTagCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(movementY, 0), -180,true);
        }
        gridAlignY = Drivebase.getPose().getY();

        return AprilTagCameraWrapper.isAlignedToCubeNode();
    }
    public static boolean alignTape(){
        // LimelightCameraWrapper.setPipeline(0);
        
        if (LimelightCameraWrapper.hasTargets()){
            double movementY = limelightAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(), Constants.AutoAlign.alignedGamePiecePitch);
            Drivebase.driveFieldRelativeHeading(new Translation2d(movementY, 0), -180,true);
        }

        return LimelightCameraWrapper.isAlignedToConeNode();
    }
    public static boolean alignToPiece(boolean isAuto){
        LimelightCameraWrapper.setPipeline(0);
        if(LimelightCameraWrapper.hasTargets()){
            double movementY = isAuto?gamePieceAutoAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset())):
                                    gamePieceTelopAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset()));
            Drivebase.driveRobotRelativeRotation(new Translation2d(-movementY,0 ), 0);
        }
        return LimelightCameraWrapper.isAlignedToGamePiece();
    }
    public static void forwardToPiece(boolean isAuto){
        if(LimelightCameraWrapper.hasTargets()){
            double movementY = isAuto?gamePieceAutoAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset())):
                                    gamePieceTelopAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset()));
            Drivebase.driveRobotRelativeRotation(new Translation2d(-movementY,isAuto?-1.25:-1.5), 0);
            // Drivebase.driveRobotRelativeRotation(new Translation2d(-movementY,0), 0);
        }else{
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,isAuto?-1.25:-1.5), 0);
            // Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
        }
    }
    public static boolean isInCommunity(){
        if (((Constants.isBlue() && (Drivebase.getPose().getX() < Constants.FieldPositions.inBlueCommunityX)) ||
            (Constants.isRed() && (Drivebase.getPose().getX() > Constants.FieldPositions.inRedCommunityX))) &&
            (Drivebase.getPose().getY() < Constants.FieldPositions.inCommunityY)){
            return true;
        }
        return false;
    }
    public static boolean isInLoadingZone(){
        if (((Constants.isBlue() && (Drivebase.getPose().getX() > Constants.FieldPositions.inBlueLoadingZoneX)) ||
            (Constants.isRed() && (Drivebase.getPose().getX() < Constants.FieldPositions.inRedLoadingZoneX))) &&
            (Drivebase.getPose().getY() > Constants.FieldPositions.inLoadingZoneY)){
            return true;
        }
        return false;
    }
    public static boolean isInCorrectLoadingZone(){
        if(Constants.isBlue()){
            if (IO.keyInputSubstationPosition.getY() + IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope - Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() > Drivebase.getPose().getY() && 
                IO.keyInputSubstationPosition.getY() - IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope + Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() < Drivebase.getPose().getY() &&
                Drivebase.getPose().getX() > Constants.FieldPositions.inBlueLoadingZoneX){
                    return true;
            }

        }else{
            if (IO.keyInputSubstationPosition.getY() + IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope - Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() < Drivebase.getPose().getY() && 
                IO.keyInputSubstationPosition.getY() - IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope + Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() > Drivebase.getPose().getY() &&
                Drivebase.getPose().getX() < Constants.FieldPositions.inRedLoadingZoneX){
                    return true;
            }

        }
        return false;
    }
    public static enum GridAlignState {
        AlignedToOdometry,
        InPosition
    }
    public static GridAlignState gridAlignState = GridAlignState.AlignedToOdometry;
    public static double gridAlignY;
    
    public static double alignRotation = -180;
    public static double alignTranslationY = 0;
    public static boolean moveToGridPositionOdometryTwoStep(){
        if(isInCommunity()){
            switch(gridAlignState) {
                case AlignedToOdometry:
                    if(alignOdometry(IO.keyInputOdometryPosition, -180)) {
                        gridAlignState = GridAlignState.InPosition;
                    }
                    //Set rotation to -180 here so that you can adjust it manunally later if needed
                    alignRotation = -180;
                    alignTranslationY = 0;
                break;
                case InPosition:
                    alignRotation = alignRotation + IO.Drivebase.getSwerveRotation() *0.05;
                    alignTranslationY = alignTranslationY + IO.Drivebase.getSwerveTranslation().getX() * (Constants.isBlue()?-0.003:0.003);
                    switch(IO.gridNodeType){
                        //If Hybrid, don't moveS
                        case Hybrid:
                            alignOdometry(IO.keyInputOdometryPosition, alignRotation);
                            return true;
                        //If Cube, don't move from normal position
                        case Cube:
                            alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.FieldPositions.noHitCubeOffset:-Constants.FieldPositions.noHitCubeOffset, 0)), alignRotation);
                            return true;
                        case ConeFar:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                //If Cone at Mid, don't move from normal position
                                case Mid:
                                    alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:Constants.FieldPositions.nearGridRedX, IO.keyInputOdometryPosition.getY() + alignTranslationY), alignRotation);
                                    return true;
                                //If Cone at High, move forward
                                case High:
                                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()), alignRotation);
                                    return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:Constants.FieldPositions.nearGridRedX, IO.keyInputOdometryPosition.getY() + alignTranslationY), alignRotation);
                            }
                            break;
                        case ConeClose:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                //If Cone at Mid, don't move from normal position
                                case Mid:
                                    return alignTranslation(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()  + alignTranslationY));
                                //If Cone at High, move forward
                                case High:
                                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()), alignRotation);
                                    return alignTranslation(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()  + alignTranslationY));
                            }
                            break;

                    }
            }
        }
        return false;
    }
    public static enum SubstationAlignState {
        AlignedToOdometry,
        InPosition
    }
    public static SubstationAlignState substationAlignState = SubstationAlignState.AlignedToOdometry;
    public static boolean moveToSubstationPosition(){
        if(isInLoadingZone()){
            switch(substationAlignState) {
                case AlignedToOdometry:
                    if (IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation || 
                        IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.redSingleSubstation){
                        if(alignOdometry(IO.keyInputSubstationPosition, 90) && GamepieceManager.runExtention()) {
                            return true;
                        }
                    }
                    // else if(isInCorrectLoadingZone()){
                    //     substationAlignState = SubstationAlignState.InPosition;
                    // }
                    else if(alignOdometrykP(IO.keyInputSubstationPosition, 0 ,3 , 3, 0.08, true) && GamepieceManager.runExtention()) {
                        substationAlignState = SubstationAlignState.InPosition;
                    }
                    break;
                case InPosition:
                    return alignOdometrykP(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atSubstationBlueX:Constants.FieldPositions.atSubstationRedX, 
                                            IO.keyInputSubstationPosition.getY()), 0, 3, 3, 0.05, true);
            }
        }
        return false;
        
    }
    public static void displayInformation(){
        SmartDashboard.putBoolean("Is in Community?", isInCommunity());
        SmartDashboard.putBoolean("Is in Loading Zone?", isInLoadingZone());

        // SmartDashboard.putString("GridAlignState", gridAlignState.toString());
        // SmartDashboard.putString("SubstationAlignState", substationAlignState.toString());
        /*  Auto Align sucks at every comp except champs until programming broke the robot :D. Also Chris is very Cool
        Who are the 4911 Cyberknights?
        Who are the 4911 CyberKnights?

While we have a facebook profile, a pretty sweet website, and invite local teams to our shop whenever the weather is sunny (so like, once?), we’ve never really had a presence here on CD.

I wanted to take some time and introduce the 4911 CyberKnights, a 5th year FRC team from Seattle, Washington.

https://lh6.googleusercontent.com/1eYyTA9o9P9RbaJillUCmhUN42ysJv-MetdgwSOnUhDBpjFmdJ_X84udFWAuCHh9Ea3qsdZOlrtmDZSWi-F1=w500-h500-rw 83

So, just how did this silly team turn up on the field at Minute Maid Park?

https://lh5.googleusercontent.com/Dkw0egqa1RhhmZpmSj9xCzkmGscWsmvWcx6KAvliT5GNPdZg5TFrFqjBHOpicmr51ukZmNP7vryfF07271Wv=w500-h500-rw 92

Even we are still awed by these past few days in Houston, and are extremely grateful and appreciative of all the support and congratulations for fighting our way into a pair of matches with an amazing alliance opposite the glass at Minute Maid Park in Houston.

I’ll take a quick part of this story to say, our success was not our own, and we have a huge network of incredible supporters who have helped us in our lab, in matches, on the sidelines, and in slack and texts over… well, 5 years! Firstly and a huuuuuge thanks to our alliance partners and friends 2910, 4499, and 5006. A collection of three awesome robots, and one simply magic swervy-groovy cubey-lifty machine. Incredible to meet and chat and cheer with all of you, I personally say thank you, and wish you all the best in the coming years, can’t wait to play with you again! But theres so much more than that.

PNW is simply a special, incredible, energetic, and fun place. From last years all-PNW alliance, to representing 5 of the 6 alliances on the Mass and Energy Fields, PNW is truly a family of teams helping each other with travel logistics, food, batteries (robot food?), scouting data, countless years of knowledge and wisdom shared freely for the benefit of enriching each other, and each and every handshake, high five, and fist-bump of good luck along the way. I would like to make specific call-outs to a few special teams who went above and beyond with their support during our first venture to Minute Maid Park - 3663 for building an amazing scouting system, 2910 for scouting with us through every match on Carver, 5803 for the knowledge they brought to our scouting meeting, the offer of tools, batteries, supplies, and especially the dinner sandwiches for our entire alliance drive team, 2557 who selflessly packed up all of our remaining gear in the pits while we were still at the ballpark competing and the truck had already arrived, 4488 for additional support of tools, food, and batteries, (not quite PNW) but 1678 for a second brand new air tank as a spare, just in case lightning struck twice, and of course 2976 for some incredible matches from the far side of the glass. Theres so many others who helped cheer, provided scouting data, brought bottles of water, and I can’t possible list you all here, but thank you!

https://lh4.googleusercontent.com/TYNo6cGfGNBS6ruS8VWnaRE9CPm_SpHM8LorxYEgeYvS58uZmwqzSTvtVpgzLnKcZidq0_YAdqoVZGnuohqu=w500-h500-rw 25

Outside of PNW, this past year 4911 was graciously invited to help found a project focused on promoting growth and success in the FRC program, and providing resources and assistance to help all teams Power Up their game! This family, The Compass Alliance, shared out another wealth of knowledge and guidance that we sucked up like a sponge as we worked alongside world-class teams to help improve team sustainability for all of FRC.

https://lh4.googleusercontent.com/LlK6vFhOe9SMIlNEDGsyX7sk0lpm9pVXRAkp9Ppp-yMqHf6IXTUQXB5_761R5CSg366Y79f9W6vfcMqPdueR=w500-h500 12

Well that seems as solid a foundation as any.

So we’ve got some awesome friends, but with all the matches, strategy, planning, execution, retrospect, learning, and improving required to earn passage onto the Einstein field, we must have done something special this year to make this happen. And I think we did.

As stated by our coach in a meeting yesterday - we cut our hours back, working only 5 days a week [instead of 7] to improve our focus. Clearly it worked, so next year we’re going to cut 4 of those days out. We’ll be winners for sure! But cutting hours did help. We had a bit more sleep, a bit more energy, and our focus did improve. We spent less of our our time unproductively, and produced more positive results. (I find some sessions of parking lot frisbee and pranks to be productive - keeps the energy positive.)

This year, alongside cutting hours, we scrunched our schedule like a bunch of madmen. Head coach told us we would have our chassis CAD finished day 3, and a driving platform on day 14. (I wish he would have told all the mentors this…) Our “finished” robot would be functional and game-ready on day 29. (Madness I tell you!)

We met the first goal.

We beat the second goal by one day.

We beat the third goal by two days. (… and still nobody told the mentors… ok fine, we knew when this happened.)

Like many teams, we build 2 robots, which have multiple names: robot 1, silverbot, pixel, wobble. And robot 2, blackbot, voxel, weeble (competition robot. Also, after the full year - Weeble wobbled, but not once did we ever fall down!!!) Building 2 robots affords us more practice time for driving, software tuning, and breaking down. We had a lot of practice breaking down. Lots. We would break down, spend 2 hours diagnosing, refining a part in CAD, CAM-ing, cutting on the mill, installing, and within 5 minutes of installation - something else would break. The process repeats. Throughout all of our competitions we had a written log for post-match robot checks. As soon as the robot hits the pit, our crew gave it a full once-over, and any problems, issues, or failures were detailed in our Quality Log. After each competition, we poured over the pages of the Quality Log, and focused our improvement efforts to prevent any repeat issues.

Throughout all of Houston competition we had only one failure when the pipe fitting on an airtank blew and we had zero pressure to support our arm and fire our climbing claw. (We think Weeble committed this act itself, in fear of getting wrangled and/or poofed in the Einstein round-robin matches…)

Our drive coach and myself came up with a game in the middle of the season. We grabbed a big whiteboard, and wrote up 10 mock robot capabilities. We drew out a field and grabbed a coin for some help with randomness. With the setup complete, we gave each other 3 “robots”, and had to come up with a game strategy to beat each other. She ended up playing this game with every student on our strategy team over the next 4 weeks, and probably played over a hundred matches before she ever stepped foot onto a field. (None of our robots specified had a 4-cube auto. Our failure to dream and imagine big is a good lesson from hindsight here. Next year I’ll make robot number 9 have a 15 game piece auto.)

I hang out with the drive team during competition. My ritual is nice and simple; fist bump, pat on the shoulder, and a call for ‘good luck’, ‘do it up’, or ‘you got this’. Our driver has a rubix cube tucked into our drivestation. He’s seen playing with it before every match. Our human player and operator make faces and joke with each other from across the field. Our drive coach prepares her last minute thoughts and ideas and calmly hugs her clipboard. I’ve reflected onto them that each match I look for 3 things - the drive teams’ heads are clear and in a good place. Theres a solid plan that we can reasonably execute on. And the robot is functional for 100% of the match. With these 3 simple things, we can perform at our highest, and achieve the best possible result. (Realistically, sometimes 1 RP was the best possible result. There are teams who had better preparation for this contingency, there are teams who didn’t. We had our method, and I wouldn’t change much about it) (edit - The best possible outcome of Einstein final number 2 would have been donuts next to 148, but we had already deployed our hook. Our driver had decided not to climb though, because he didn’t want to have to carry the robot down. It was a long, tiring day. I wish we had done donuts…)

Autonomous has proven to be absolutely key in almost every recent FRC game. Our lead software programmer started early, working with a wooden board on wheels to write our auto path code. It was slow, and clunky. Our first meet we were the only bot that scored a cross-field scale auto. That was cool to watch. After every competition, our software team tweaked and tuned and improved all of our autos. More reliable, faster, more cubes. They also met with our drive team, scouting team, and strategy team to come up with the concepts for new autonomous modes. We looked at minor improvements like, should the arm be up or down after spitting a cube? Should we turn the robot around? Should we go halfway across the back of the switch and then stop to make sure we dont block an alliance partner running a multiple-cube auto? After watching all of our autonomous practice in the lab before District Champs, I declared to our software lead that we will never lift and score air. He thought the auto’s could be reliable enough and took me up on a little wager - every time the robot failed to grab the second cube, he had to go meet someone from another team and find out 3 things about them. He got back to his laptop and worked hard to make sure the auto’s were super reliable. At Houston due to this wager, he had to meet one person, and indeed met a programmer on team 3132. (Two programmers talking… I’m still not sure I believe him…)

One very specific attribute to the success of our Einstein Alliance, was matching our 1-cube wall auto with 2910’s 2-cube cross field auto. (I giggled every time it happened. I giggled a lot.) Many individual teams had 3-cube autos. Many individual teams had pretty good reliability. At the end of auto, we would more often than not have 3 cubes in the scale, and importantly, 2 robots starting tele-op from the scale position. Our first cubes were super close so we could start collecting and fighting for the scale.

District events are madness. Often times as soon as you come off the field, the queuer is waiting in your pit to send you right back into the melee. District Championships and World Championships are somewhat more relaxing, given the copious amount of time between some matches. This year we changed up our process a little with our post-match meetings. Our full drive team, strategy team, an on-call engineering mentor, software lead, and match videographer would all huddle into a corner, discuss in depth how the previous match played out, and start formulating a strategy for the next match. We would bring in an older Surface laptop and re-watch specific portions of the match, identifying just how the planned strategy matched the real alliance performance. We involved our student software lead and an on-call engineering mentor to cover topics such as our autonomous success or failures, and any robot issues that came about during the match. Once we started this process in Portland, it radically shifted how we approached our pre-match alliance discussions, and it improved our driver and operator performance as they could learn from every match, identify post-auto drive paths, improve on-field communication, and just get better and better at stacking. Every relationship requires a foundation of solid communication. (Said the un-married guy. But don’t worry, I’m fixing that in 2 months!) These post-match discussions provided us with a single channel of clear communication to call out positive plays, learn from mistakes, and ensure that our robot functionality goals were met.

Our climber went through 15 iterations. Which is a bit odd, given that just last year, we did some math, figured out some gear ratios and motor power for how to winch up ~150 pounds. And this year well, we stole from our last years selves when designing the winch. (We literally stole the motors and gear spockets from a spare that we still had lying around…) But 15 iterations, some paper shims, a few cut ropes, and an idea origininally pulled from team 118’s 2016 robot led to some spectacular climbs, all in under 6 seconds. I was told at dinner before flying out of Houston that back in Portland, our climber lead designer had intentionally re-wound the tensioning cord on the claw, to make the loudest THWACK possible when the two-piece design clamped down on the scale bar. Some teams like satisfyingly floopy features, this year we went for satisfyingly THWACK-y.

A next note is kind of an interesting one - but something I do like to point out, and something I feel greatly helps those teams who are able to accomodate this: We CyberKnights have all 5 of our competition robots from all 5 of our years, assembled, drive-ready (mostly). We CyberKnights came from 4 years of FTC, and we still have all 4 robots assembled, and drive ready (also, mostly). Several times this season and last, I’ve had students ask how to conceptualize a component, how to mount a bracket, or similar. I casually walked over through our heritage museum (tucked into the darkest corner under all the dust) and would often find something similar and say “Hey, check out what Daniel did 2 years ago. Hows that look?” to which they would almost always reply “…” and then return to putting more clementines into the microwave or freezer (depending on the day). Having our entire collection of robots gives us examples of mechanisms, and for the older students, some recollection of what we liked and disliked about certain robot features in the past.

This year was our first year with a mentor fully supporting our scouting and data collection team. In the way past, we couldn’t scout our way out of a plastic bag. In the recent past we could identify a few solid picks, but we didn’t have anyone really checking on the insubstantial data, or to soundboard thoughts and ideas about the underlying trends beneath the immediate and obvious data. And of course, it’s always necessary to verify that the data collected was accurate and meaningful. (Yeah team 8319 scored 14 cubes in the vault last match. … Are ya sure? Also, who’s 8319?) Having a mentor here empowered our lead scout to better understand how to watch a robot and provide meaningful qualitative data about that robot and/or drive team decisions. The quantitative data collection process smoothed out and increased in pace, which meant we could get accurate data to our drive team faster, giving them more time to prepare for upcoming matches.

Outside of proper mentorship however, our lead scout owned our Houston pick-list. Friday night after dinner we had our traditional pick-list meeting with scouts, drive coach, and strategy group. (For this game our climber design lead and intake design lead became assistant drive coach and head of strategy for the competition season.) With a few additional PNW guests, we studied, schemed, complained that the HDMI port on the hotel tv wasn’t working, and crafted a plan. The outcome was what appeared to be a solid list, that may have been rushed to completion due to the late hour. The brain trust left and all went to sleep. Almost. Unsatisfied that we hadn’t taken all factors about climb success and min/max/average cube scores into our list of second and third round picks, our head of strategy burned the midnight oil to slightly alter and re-position several teams on the list. At 3am he woke our head of strategy to go over the changes and the reasons for each change. They then got 3 hours sleep, and at 6am presented the updated list to the strategy mentor and drive coach before the all-important alliance selection. Most of the team didn’t know about this until Sunday, but it makes me proud to see the dedication and end-to-end ownership that our kids have in their areas of focus.

Our head coach is a maniac. I mean, anyone in this business is. It’s a good thing. I promise. His drive and passion (and sometimes over-zealous competitiveness) are constant motivators and model qualities that help steer the CyberKnight ship for the students, mentors, parents, and other faculty/administration at the school. There’s a perfect blend that is constantly challenged by all teachers and mentors for when and how much to step in and support the young-ens, versus when to just drop em in the ocean like an apollo 13 capsule and hope the kid can open the chute on their own. Our coach isn’t perfect, and trips on the line sometimes. But more often than not, he produces students who have pride and ownership in their part of the robot. They drive progress from prototypes, CAD, machining, assembly, to operations/maintenance and iterative improvement. Alongside ownership, his method produces kids with grit. One night in week 4 (it’s all a little fuzzy now) we got a drop shipment of our robot 2 parts back from the annodizing company. At 5pm, coach made a declaration: We are not leaving tonight until this robot is assembled and driving. I took my turn and made my own proclamation: We dont have time to make mistakes. Take your time with every bend, every cleco, every rivet. Know the full process and build order. If anything looks wonky - call it out, get someone to help look at it. … Needless to say, by midnight, we found a piece of wonkiness and had to drill out, twist, clamp, and re-rivit one of the main tubes on our tower. 3am came soon after - with a driving robot. The next day the kids came into the lab, tired, but determined. Our head coach is a madman. But in a good way. (Some people reading may think this is cruel and/or un-necessary. My first job out of school was working for Amazon. My experience in FRC helped me excel, because I knew that I could push myself to deliver results. I had multiple consecutive 3am days to deliver on project deadlines. I was prepared. I had grit. I delivered. Thanks Tough Techs 151 for preparing me.)

The CyberKnights are a 5th year team from Seattle WA. We are pushed hard by a head coach who knows what we are capable of. We focus and work smart to deliver on our goals. We are supported by an incredible network of teams from all across the globe. We love to share out what we can, soak in what learning we can like a bunch of red and black sponges, and work together with others, conversing in the universal language of robots. And we do it all with a big grin on our face.


-B
*/
    

    }
}
