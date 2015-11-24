package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.util.Constants;

public class Cross extends Object3D {
    private Render game;

    public Cross(Render game) {
        super(Primitives.getPlane(2, 2f));
        translate(0, -2, 0);
        setTexture("crosst");
        setBillboarding(true);
        setTransparency(20);
        setTransparencyMode(TRANSPARENCY_MODE_ADD);
        setCollisionMode(COLLISION_CHECK_OTHERS);
        CrossCollision crossCollision = new CrossCollision();
        addCollisionListener(crossCollision);
        this.game = game;
    }


    public class CrossCollision implements CollisionListener {

        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                if (arg0.getSource() == null) {
                    setCollisionMode(COLLISION_CHECK_NONE);
                } else {
                    setCollisionMode(COLLISION_CHECK_OTHERS);
                }
                for (int i = 0; i < 5; i++) {
                    new Effect(game, Cross.this.getTransformedCenter(), Constants.PARTICLES);
                }
                game.world.removeObject(Cross.this);
                if (game.stage == Constants.STAGE_7) {
                    game.castle.doors[8].activate();
                    game.stage7Step++;
                } else if (game.stage == Constants.STAGE_9) {
                    game.castle.doors[7].activate();
                    game.castle.doors[6].activate();
                    game.stage9Step++;
                }
                game.mode = Constants.MODE_NORMAL;
                game.crosses++; //se registra la obtenci�n de la nueva cruz
                if (game.crosses == 2) {
                    game.castle.doors[3].activate();
                }
                for (int i = 0; i < 5; i++) {
                    new Effect(game, Cross.this.getTransformedCenter(), Constants.PARTICLES);
                }
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

}
