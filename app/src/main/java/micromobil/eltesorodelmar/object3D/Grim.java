package micromobil.eltesorodelmar.object3D;


import com.threed.jpct.Camera;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.util.Constants;

public class Grim extends Object3D {
    int state;
    float ind;
    int count;
    int face;
    private Render game;

    public Grim(Render game) {
        super(Loader.loadSerializedObject(game.getResources().openRawResource(R.raw.grim2)));
        rotateX(-(float) Math.PI / 2);
        rotateY(-(float) Math.PI / 2);
        setTexture("grimt");
        System.out.println("grim");
        setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        GrimCollision grimCollision = new GrimCollision();
        addCollisionListener(grimCollision);
        this.game = game;

        strip();
        build();
    }

    public class GrimCollision implements CollisionListener {
        @Override
        public void collision(CollisionEvent arg0) {
            if (arg0 != null && arg0.getSource() == game.pirate) {
                game.mode = Constants.MODE_CINEMATICS;
                game.pirate.state = Constants.STAND;
                Camera sCam = new Camera();
                sCam.setPositionToCenter(game.pirate);
                SimpleVector sv = game.pirate.getRotationMatrix().getZAxis();
                sv.rotateY((float) Math.PI / 2);
                sCam.moveCamera(sv, 5);
                sCam.lookAt(Grim.this.getTransformedCenter());
                game.world.setCameraTo(sCam);
                Grim.this.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
                game.cinematics.index = 0;
            }
        }

        @Override
        public boolean requiresPolygonIDs() {
            return false;
        }
    }

}
