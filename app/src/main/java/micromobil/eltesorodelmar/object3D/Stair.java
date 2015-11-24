package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Stair extends Object3D {
    int state;
    float ind;
    int count;
    private Render game;

    public Stair(Render game) {
        super(Loader.loadMD2(game.getResources().openRawResource(R.raw.ladder), 0.15f));
        state = Constants.DOOR_STAND;
        setTexture("laddert");
        StairCollision stairCollision = new StairCollision();
        setCollisionMode(COLLISION_CHECK_OTHERS);
        addCollisionListener(stairCollision);
        this.game = game;

        strip();
        build();
    }

    public void animate() {
        switch (state) {
            case Constants.DOOR_STAND: {
                animate(0, 1);
                break;
            }
            case Constants.DOOR_OPEN: {
                ind += 0.016f;
                if (ind > 0.8) {
                    ind = 0;
                    state = Constants.DOOR_STAND2;
                } else {
                    animate(ind, 2);
                }
                break;
            }
            case Constants.DOOR_CLOSE: {
                ind += 0.016f;
                if (ind > 0.8) {
                    ind = 0;
                    state = Constants.DOOR_STAND;
                } else {
                    animate(ind, 3);
                }
                break;
            }
            case Constants.DOOR_STAND2: {
                animate(0, 4);
                break;
            }
        }
    }

    public class StairCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (count <= 0)
                count = Constants.COUNT;

            if (arg0 != null) {
                int[] poligons = arg0.getPolygonIDs();
                for (int i = 0; i < poligons.length; i++) {
                    System.out.print(poligons[i] + ", ");
                }
                System.out.println();
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return true;
        }
    }

}
