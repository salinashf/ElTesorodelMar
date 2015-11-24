package micromobil.eltesorodelmar.object3D;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Bottle extends Object3D {
    private Render game;

    public Bottle(Render game) {
        super(Primitives.getPlane(2, 2f));
        game.soundManager.addSound(Constants.DRINK, R.raw.sdrink);
        setTexture("ront");
        setBillboarding(true);
        setTransparency(20);
        setTransparencyMode(TRANSPARENCY_MODE_ADD);
        setCollisionMode(COLLISION_CHECK_OTHERS);
        BottleCollision bottleCollision = new BottleCollision();
        addCollisionListener(bottleCollision);
        this.game = game;
    }


    public class BottleCollision implements CollisionListener {

        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null) {
                if (arg0.getSource() == null) {
                    setCollisionMode(COLLISION_CHECK_NONE);
                } else {
                    setCollisionMode(COLLISION_CHECK_OTHERS);
                }
                game.soundManager.playSound(Constants.DRINK);
                if (game.pirate.damage >= 40)
                    game.pirate.damage -= 40;
                else
                    game.pirate.damage -= game.pirate.damage;
                for (int i = 0; i < 5; i++) {
                    new Effect(game, Bottle.this.getTransformedCenter(), Constants.PARTICLES);
                }
                Bottle.this.setVisibility(false);
            }


        }

        @Override
        public boolean requiresPolygonIDs() {
            // TODO Auto-generated method stub
            return false;
        }
    }

}
