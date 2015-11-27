package micromobil.eltesorodelmar.util;

import com.threed.jpct.SimpleVector;

public class Constants {
    public static final String LOG_APP ="ElTesosoDelMar" ;
    public static final int STAGE_1 = 0;
    public static final int STAGE_2 = 1;
    public static final int STAGE_3 = 2;
    public static final int STAGE_4 = 3;
    public static final int STAGE_5 = 4;
    public static final int STAGE_6 = 5;
    public static final int STAGE_7 = 6;
    public static final int STAGE_8 = 7;
    public static final int STAGE_9 = 8;
    public static final int STAGE_10 = 9;
    public static final int STAGE_11 = 10;

    public static final int CACHE_RADIUS = 15;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_GUN = 1;
    public static final int MODE_CINEMATICS = 2;
    public static final int MODE_DEATH = 3;

    public static final int STAND = 0;
    public static final int WALK = 1;
    public static final int JUMP = 3;
    public static final int PRECLIMB = 4;
    public static final int FALL = 5;
    public static final int ATK1 = 6;
    public static final int PUSH = 7;
    public static final int PAIN = 8;
    public static final int DEATH = 9;
    public static final int SHOOT = 10;
    public static final int DRINK = 11;
    public static final int PAIN_BY_KNIGHT = 12;
    public static final int PAIN_BY_DEVIL = 13;

    public static final int DOOR_OPEN = 110;
    public static final int DOOR_CLOSE = 111;
    public static final int DOOR_STAND = 112;
    public static final int DOOR_STAND2 = 113;

    public static final float MOV_SPEED = 0.5f;
    public static final float CAM_DIST = 15.0f;

    public static final float JUMP_LENGTH = 0.6f;
    public static final float JUMP_HEIGHT = 0.2f;

    public static final float MOV_SPEED_KNIGHT = 0.25f;
    public static final float ENEMY_DIST_MAX = 7f;

    public static final int KNIGHT_DAMAGE = 5;
    public static final int DEVIL_DAMAGE = 5;

    public static final SimpleVector ELLIPSOID = new SimpleVector(2, 2.5f, 2);
    public static final SimpleVector CAM_ELLIPSOID = new SimpleVector(0.1, 0.1, 0.1);
    public static final SimpleVector BOX_ELLIPSOID = new SimpleVector(4, 4, 4);
    public static final SimpleVector KNIGHT_ELLIPSOID = new SimpleVector(4, 3, 4);

    public static final int RECURSION = 5;

    public static final int NONE = 0;
    public static final int N = 1;
    public static final int S = 2;
    public static final int W = 3;
    public static final int E = 4;
    public static final int NW = 5;
    public static final int NE = 6;
    public static final int SW = 7;
    public static final int SE = 8;

    public static final int FIRE = 0;
    public static final int DUST = 1;
    public static final int DUSTR = 2;
    public static final int PARTICLES = 3;

    public static final int COUNT = 50;

    public static final int GUILLOTINE_MOVE = 1;
    public static final int SAW_MOVE = 1;
    public static final int BOX_MOVE = 1;

    public static final int FIRE_NUMBER = 10;

}
