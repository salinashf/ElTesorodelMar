package micromobil.eltesorodelmar.object3D.load;

import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import java.io.InputStream;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.object3D.Bottle;
import micromobil.eltesorodelmar.object3D.Box;
import micromobil.eltesorodelmar.object3D.Bridge;
import micromobil.eltesorodelmar.object3D.Devil;
import micromobil.eltesorodelmar.object3D.Door;
import micromobil.eltesorodelmar.object3D.Effect;
import micromobil.eltesorodelmar.object3D.FlyingFire;
import micromobil.eltesorodelmar.object3D.Grim;
import micromobil.eltesorodelmar.object3D.Guillotine;
import micromobil.eltesorodelmar.object3D.Head;
import micromobil.eltesorodelmar.object3D.Knight;
import micromobil.eltesorodelmar.object3D.Saw;
import micromobil.eltesorodelmar.object3D.Switch;
import micromobil.eltesorodelmar.util.Constants;

public class Castle {

    public Object3D castle;
    public Object3D castleCache;
    public int castleStage; //stage que contiene Object3D castle
    public Door[] doors;
    public Switch[] switches;
    public Guillotine[] guillotines;
    public Saw[] saws;
    public Box[] boxes;
    public FlyingFire[] ff3;
    public FlyingFire[] ff6;
    public Knight knight; //caballero
    public Grim[] grims;
    public Bridge bridge;
    public Head[] heads;
    public Devil devil;
    public Bottle[] bottles;
    public Object3D[] banners;
    public Object3D[] torches;
    public Effect[] torchesFire;//y = -3.3, z + 2.5
    public FlyingFireSpreadingThread fftStage3;
    public FlyingFireSpreadingThread fftStage6;
    public SceneSwitchThread sceneSwitcher;
    Render game;

    public Castle(Render game) {
        this.game = game;

        doors = new Door[11];
        switches = new Switch[5];
        guillotines = new Guillotine[3];
        saws = new Saw[3];
        boxes = new Box[10];
        boxes = new Box[7];
        grims = new Grim[2];
        heads = new Head[2];
        bottles = new Bottle[6];
        ff3 = new FlyingFire[Constants.FIRE_NUMBER];
        ff6 = new FlyingFire[Constants.FIRE_NUMBER];
        banners = new Object3D[6];
        torches = new Object3D[6];
        torchesFire = new Effect[8];

        //cargar puerta a scene 2
        doors[0] = new Door(game, 1); //la puerta se crea abierta
        doors[0].translate(0, 0, 64.3f);

        //cargar puerta a scene 3
        doors[1] = new Door(game, 0);
        doors[1].translate(-82.1f, -18.5f, 222.8f);
        doors[1].rotateY((float) Math.PI / 2);

        //cargar puerta a scene 4
        doors[2] = new Door(game, 0);
        doors[2].translate(81.4f, -18.5f, 221.8f);
        doors[2].rotateY(-(float) Math.PI / 2);

        //cargar puerta a scene 5
        doors[3] = new Door(game, 0);
        doors[3].translate(0.0f, -18.5f, 304.5f);
        doors[3].state = Constants.DOOR_OPEN;

        //cargar puerta a scene 6
        doors[4] = new Door(game, 1);
        doors[4].translate(-106.0f, -18.5f, 454.6f);

        //cargar puerta a scene 6
        doors[5] = new Door(game, 1);
        doors[5].translate(-107.0f, -19.5f, 529.8f);
        doors[5].rotateY(-(float) Math.PI);

        //cargar puerta a scene 8
        doors[6] = new Door(game, 1);
        doors[6].translate(115f, -18.5f, 365f);

        //cargar puerta a scene 9
        doors[7] = new Door(game, 0);
        doors[7].rotateY(-(float) Math.PI);
        doors[7].translate(138.8f, -18.7f, 515f);

        //cargar puerta a scene 7
        doors[8] = new Door(game, 1);
        doors[8].translate(-106.0f, 3.6f, 678.0f);

        //cargar puerta a scene 10
        doors[9] = new Door(game, 0);
        doors[9].rotateY(-(float) Math.PI);
        doors[9].translate(2f, -18.8f, 455.4f);

        //cargar puerta a scene 11
        doors[10] = new Door(game, 0);
        doors[10].rotateY(-(float) Math.PI);
        doors[10].translate(2f, -39f, 601f);

        //iniciar scene
        this.sceneTransport(Constants.STAGE_1);

        for (int i = 0; i < doors.length; i++)
            doors[i].setVisibility(false);

        //agregar elementos al mundo
        game.world.addObject(doors[0]);
        game.world.addObject(doors[1]);
        game.world.addObject(doors[2]);
        game.world.addObject(doors[3]);
        game.world.addObject(doors[4]);
        game.world.addObject(doors[5]);
        game.world.addObject(doors[6]);
        game.world.addObject(doors[7]);
        game.world.addObject(doors[8]);
        game.world.addObject(doors[9]);
        game.world.addObject(doors[10]);

        sceneSwitcher = new SceneSwitchThread();
        sceneSwitcher.start();
    }

    public void sceneTransport(int scene) {
        game.pirate.clearTranslation();
        switch (scene) {
            case Constants.STAGE_1:
                game.pirate.translate(0, -10, -56.0f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle1ser));
                break;
            case Constants.STAGE_2:
                game.pirate.translate(0.0f, -18.5f, 290.5f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle2ser));
                break;
            case Constants.STAGE_3:
                game.pirate.translate(-92.1f, -18.5f, 222.8f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle3ser));
                break;
            case Constants.STAGE_5:
                game.pirate.translate(2f, -18.8f, 445.4f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle5ser));
                break;
            case Constants.STAGE_6:
                game.pirate.translate(-106.0f, -18.5f, 549.8f);
                game.stage6Step = 1;
                this.loadCastle(game.getResources().openRawResource(R.raw.castle6ser));
                break;
            case Constants.STAGE_7:
                game.pirate.translate(-106.0f, 3.6f, 688.0f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle7ser));
                break;
            case Constants.STAGE_9:
                game.pirate.translate(138.8f, -18.7f, 525f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle9ser));
                break;
            case Constants.STAGE_10:
                game.pirate.translate(2f, -39f, 591f);
                this.loadCastle(game.getResources().openRawResource(R.raw.castle10ser));
                break;
        }
        game.stage = scene;
        castleStage = scene;
        this.addObjectsFromStage(scene);
        game.pirate.moveCameraBehind();
        game.lamp.enable(); //ilumina la habitaci�n
        game.world.setFogging(World.FOGGING_DISABLED); //deshabilita la niebla
    }

    public void loadCastle(InputStream is) {
        castle = Loader.loadSerializedObject(is);
        castle.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        game.world.addObject(castle);
        castle.build();
        castle.strip();
    }

    public void loadCastleCache(InputStream is) {
        castleCache = Loader.loadSerializedObject(is);
        castleCache.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
        game.world.addObject(castleCache);
        castleCache.build();
        castleCache.strip();
    }

    //caso de uso: Cargar escena en cach�
    public void loadCache() {
        loadCache(game.stage);
        sceneSwitcher();
    }

    public void loadCache(int stage) {
        actualizeDoors(stage);
        switch (stage) {
            case Constants.STAGE_1:
                loadCacheStage1();
                break;
            case Constants.STAGE_2:
                loadCacheStage2();
                break;
            case Constants.STAGE_3:
                loadCacheStage3();
                break;
            case Constants.STAGE_4:
                loadCacheStage4();
                break;
            case Constants.STAGE_5:
                loadCacheStage5();
                break;
            case Constants.STAGE_6:
                loadCacheStage6();
                break;
            case Constants.STAGE_7:
                loadCacheStage7();
                break;
            case Constants.STAGE_8:
                loadCacheStage8();
                break;
            case Constants.STAGE_9:
                loadCacheStage9();
                break;
            case Constants.STAGE_10:
                loadCacheStage10();
                break;
            case Constants.STAGE_11:
                loadCacheStage11();
                break;
        }
    }

    public void loadCacheStage1() {
        if (castleCache == null && castleStage == Constants.STAGE_1 && game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 1 y se acerca a la puerta a stage 2
            loadCastleCache(game.getResources().openRawResource(R.raw.castle2ser));
        } else if (game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleCache != null && castleStage == Constants.STAGE_1 && game.world.getObject(castleCache.getID()) != null) {
                //en stage 1 y se aleja de la puerta a stage 2 con stage 2 en cache
                game.world.removeObject(castleCache);
                castleCache = null;
            } else if (castleStage == Constants.STAGE_2) {
                //en stage 1 y se aleja de la puerta a stage 2 con stage 1 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_1;
                castleCache = null;
            }
        }
    }

    public void loadCacheStage2() {
        if (game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_2) {
                //en stage 2 y se aleja de la puerta a stage 1 con stage 1 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 2 y se aleja de la puerta a stage 1 con stage 2 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_2;
                castleCache = null;
            }
        } else if (castleStage == Constants.STAGE_2 && game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 2 y se acerca de la puerta a stage 1
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle1ser));
            }
        } else if (castleStage == Constants.STAGE_2 && game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 2 y se acerca de la puerta a stage 3
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle3ser));
            }
        } else if (castleStage == Constants.STAGE_2 && game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 2 y se acerca de la puerta a stage 4
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle4ser));
            }
        } else if (castleStage == Constants.STAGE_2 && game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 2 y se acerca de la puerta a stage 5
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle5ser));
            }
        }
    }

    public void loadCacheStage3() {
        if (game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_3) {
                //en stage 3 y se aleja de la puerta a stage 2 con stage 2 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 3 y se aleja de la puerta a stage 2 con stage 3 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_3;
                castleCache = null;
            }
        } else if (castleCache == null && castleStage == Constants.STAGE_3 && game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 3 y se acerca a la puerta a stage 2
            loadCastleCache(game.getResources().openRawResource(R.raw.castle2ser));
        } else if (castleCache == null && castleStage == Constants.STAGE_3 && game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 3 y se acerca de la puerta a stage 6
            loadCastleCache(game.getResources().openRawResource(R.raw.castle6ser));
        }
    }

    public void loadCacheStage4() {
        if (game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_4) {
                //en stage 4 y se aleja de la puerta a stage 2 con stage 2 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 4 y se aleja de la puerta a stage 2 con stage 4 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_4;
                castleCache = null;
            }
        } else if (castleStage == Constants.STAGE_4 && game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 4 y se acerca de la puerta a stage 2
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle2ser));
            }
        } else if (castleStage == Constants.STAGE_4 && game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 4 y se acerca de la puerta a stage 8
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle8ser));
            }
        }
    }

    public void loadCacheStage5() {
        if (game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_5) {
                //en stage 5 y se aleja de la puerta a stage 2 con stage 2 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 5 y se aleja de la puerta a stage 2 con stage 5 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_5;
                castleCache = null;
            }
        } else if (castleStage == Constants.STAGE_5 && game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 5 y se acerca de la puerta a stage 2
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle2ser));
            }
        } else if (castleStage == Constants.STAGE_5 && game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 5 y se acerca de la puerta a stage 10
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle10ser));
            }
        }
    }

    public void loadCacheStage6() {
        if (game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_6) {
                //en stage 6 y se aleja de la puerta a stage 3 con stage 3 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 6 y se aleja de la puerta a stage 3 con stage 6 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_6;
                castleCache = null;
            }
        } else if (castleCache == null && castleStage == Constants.STAGE_6 && game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 6 y se acerca a la puerta a stage 3
            loadCastleCache(game.getResources().openRawResource(R.raw.castle3ser));
        } else if (castleCache == null && castleStage == Constants.STAGE_6 && game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 6 y se acerca a la puerta a stage 7
            System.out.println("load7");
            loadCastleCache(game.getResources().openRawResource(R.raw.castle7ser));
        }
    }

    public void loadCacheStage7() {
        if (game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleCache != null && castleStage == Constants.STAGE_7) {
                //en stage 7 y se aleja de la puerta a stage 6 con stage 6 en cache
                game.world.removeObject(castleCache);
                castleCache = null;
            } else if (castleStage == Constants.STAGE_6) {
                //en stage 7 y se aleja de la puerta a stage 6 con stage 7 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_7;
                castleCache = null;
            }
        } else if (castleCache == null && castleStage == Constants.STAGE_7 && game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 7 y se acerca a la puerta a stage 6
            loadCastleCache(game.getResources().openRawResource(R.raw.castle6ser));
        }
    }

    public void loadCacheStage8() {
        if (game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_8) {
                //en stage 8 y se aleja de la puerta a stage 4 con stage 4 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 8 y se aleja de la puerta a stage 4 con stage 8 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_8;
                castleCache = null;
            }
        } else if (castleStage == Constants.STAGE_8 && game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 8 y se acerca de la puerta a stage 4
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle4ser));
            }
        } else if (castleStage == Constants.STAGE_8 && game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 8 y se acerca de la puerta a stage 9
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle9ser));
            }
        }
    }

    public void loadCacheStage9() {
        if (castleCache == null && castleStage == Constants.STAGE_9 && game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 9 y se acerca a la puerta a stage 8
            loadCastleCache(game.getResources().openRawResource(R.raw.castle8ser));
        }
        if (game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleCache != null && castleStage == Constants.STAGE_9 && game.world.getObject(castleCache.getID()) != null) {
                //en stage 9 y se aleja de la puerta a stage 8 con stage 8 en cache
                game.world.removeObject(castleCache);
                castleCache = null;
            } else if (castleStage == Constants.STAGE_8) {
                //en stage 9 y se aleja de la puerta a stage 8 con stage 9 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_9;
                castleCache = null;
            }
        }
    }

    public void loadCacheStage10() {
        if (game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() > Constants.CACHE_RADIUS && game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleStage == Constants.STAGE_10) {
                //en stage 10 y se aleja de la puerta a stage 5 con stage 5 en cache
                if (castleCache != null) {
                    game.world.removeObject(castleCache);
                    castleCache = null;
                }
            } else {
                //en stage 10 y se aleja de la puerta a stage 5 con stage 10 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_10;
                castleCache = null;
            }
        } else if (castleStage == Constants.STAGE_10 && game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 10 y se acerca de la puerta a stage 5
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle5ser));
            }
        } else if (castleStage == Constants.STAGE_10 && game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 10 y se acerca de la puerta a stage 11
            if (castleCache == null) {
                loadCastleCache(game.getResources().openRawResource(R.raw.castle11ser));
            }
        }
    }

    public void loadCacheStage11() {
        if (castleCache == null && castleStage == Constants.STAGE_11 && game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            //en stage 11 y se acerca a la puerta a stage 10
            loadCastleCache(game.getResources().openRawResource(R.raw.castle10ser));
        }
        if (game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() > Constants.CACHE_RADIUS) {
            if (castleCache != null && castleStage == Constants.STAGE_11 && game.world.getObject(castleCache.getID()) != null) {
                //en stage 11 y se aleja de la puerta a stage 10 con stage 10 en cache
                game.world.removeObject(castleCache);
                castleCache = null;
            } else if (castleStage == Constants.STAGE_10) {
                //en stage 11 y se aleja de la puerta a stage 10 con stage 11 en cache
                if (castle != null && game.world.getObject(castle.getID()) != null) {
                    game.world.removeObject(castle);
                }
                castle = castleCache;
                castleStage = Constants.STAGE_11;
                castleCache = null;
            }

        }
    }

    public synchronized void sceneSwitcher() {
        //de stage 2 a stage 1
        if (game.stage != Constants.STAGE_1 && game.pirate.getTransformedCenter().z < doors[0].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_1;
            removeObjectsFromStage(Constants.STAGE_2);
            addObjectsFromStage(Constants.STAGE_1);
            //de stage 1 a stage 2
        } else if (game.stage != Constants.STAGE_2 && game.pirate.getTransformedCenter().z > doors[0].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[0].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_2;
            removeObjectsFromStage(Constants.STAGE_1);
            addObjectsFromStage(Constants.STAGE_2);
            //de stage 3 a stage 2
        } else if (game.stage != Constants.STAGE_2 && game.pirate.getTransformedCenter().x > doors[1].getTransformedCenter().x && game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_2;
            removeObjectsFromStage(Constants.STAGE_3);
            addObjectsFromStage(Constants.STAGE_2);
            //de stage 4 a stage 2
        } else if (game.stage != Constants.STAGE_2 && game.pirate.getTransformedCenter().x < doors[2].getTransformedCenter().x && game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_2;
            removeObjectsFromStage(Constants.STAGE_4);
            addObjectsFromStage(Constants.STAGE_2);
            //de stage 5 a stage 2
        } else if (game.stage != Constants.STAGE_2 && game.pirate.getTransformedCenter().z < doors[3].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_2;
            removeObjectsFromStage(Constants.STAGE_5);
            addObjectsFromStage(Constants.STAGE_2);
            //de stage 2 a stage 3
        } else if (game.stage != Constants.STAGE_3 && game.pirate.getTransformedCenter().x < doors[1].getTransformedCenter().x && game.pirate.getTransformedCenter().calcSub(doors[1].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_3;
            removeObjectsFromStage(Constants.STAGE_2);
            addObjectsFromStage(Constants.STAGE_3);
            //de stage 2 a stage 4
        } else if (game.stage != Constants.STAGE_4 && game.pirate.getTransformedCenter().x > doors[2].getTransformedCenter().x && game.pirate.getTransformedCenter().calcSub(doors[2].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_4;
            removeObjectsFromStage(Constants.STAGE_2);
            addObjectsFromStage(Constants.STAGE_4);
            //de stage 2 a stage 5
        } else if (game.stage != Constants.STAGE_5 && game.pirate.getTransformedCenter().z > doors[3].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[3].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_5;
            removeObjectsFromStage(Constants.STAGE_2);
            addObjectsFromStage(Constants.STAGE_5);
            //de stage 3 a stage 6
        } else if (game.stage != Constants.STAGE_6 && game.pirate.getTransformedCenter().z > doors[5].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_6;
            removeObjectsFromStage(Constants.STAGE_3);
            addObjectsFromStage(Constants.STAGE_6);
            if (game.stage6Step == 0) {
                game.mode = Constants.MODE_CINEMATICS;
            }
            //de stage 6 a stage 3
        } else if (game.stage != Constants.STAGE_3 && game.pirate.getTransformedCenter().z < doors[5].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[5].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_3;
            removeObjectsFromStage(Constants.STAGE_6);
            addObjectsFromStage(Constants.STAGE_3);
            //de stage 6 a stage 7
        } else if (game.stage != Constants.STAGE_7 && game.pirate.getTransformedCenter().z > doors[8].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_7;
            removeObjectsFromStage(Constants.STAGE_6);
            addObjectsFromStage(Constants.STAGE_7);
            //de stage 7 a stage 6
        } else if (game.stage != Constants.STAGE_6 && game.pirate.getTransformedCenter().z < doors[8].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[8].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_6;
            removeObjectsFromStage(Constants.STAGE_7);
            addObjectsFromStage(Constants.STAGE_6);
            //de stage 4 a stage 8
        } else if (game.stage != Constants.STAGE_8 && game.pirate.getTransformedCenter().z > doors[6].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_8;
            removeObjectsFromStage(Constants.STAGE_4);
            addObjectsFromStage(Constants.STAGE_8);
            //de stage 8 a stage 4
        } else if (game.stage != Constants.STAGE_4 && game.pirate.getTransformedCenter().z < doors[6].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[6].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            game.stage = Constants.STAGE_4;
            removeObjectsFromStage(Constants.STAGE_8);
            addObjectsFromStage(Constants.STAGE_4);
            //de stage 8 a stage 9
        } else if (game.stage != Constants.STAGE_9 && game.pirate.getTransformedCenter().z > doors[7].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 9");
            game.stage = Constants.STAGE_9;
            removeObjectsFromStage(Constants.STAGE_8);
            addObjectsFromStage(Constants.STAGE_9);
            //de stage 9 a stage 8
        } else if (game.stage != Constants.STAGE_8 && game.pirate.getTransformedCenter().z < doors[7].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[7].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 8");
            game.stage = Constants.STAGE_8;
            removeObjectsFromStage(Constants.STAGE_9);
            addObjectsFromStage(Constants.STAGE_8);
            //de stage 5 a stage 10
        } else if (game.stage != Constants.STAGE_10 && game.pirate.getTransformedCenter().z > doors[9].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 10");
            game.stage = Constants.STAGE_10;
            removeObjectsFromStage(Constants.STAGE_5);
            addObjectsFromStage(Constants.STAGE_10);
            //de stage 10 a stage 5
        } else if (game.stage != Constants.STAGE_5 && game.pirate.getTransformedCenter().z < doors[9].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[9].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 5");
            game.stage = Constants.STAGE_5;
            removeObjectsFromStage(Constants.STAGE_10);
            addObjectsFromStage(Constants.STAGE_5);
            //de stage 10 a stage 11
        } else if (game.stage != Constants.STAGE_11 && game.pirate.getTransformedCenter().z > doors[10].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 11");
            game.stage = Constants.STAGE_11;
            removeObjectsFromStage(Constants.STAGE_10);
            addObjectsFromStage(Constants.STAGE_11);
            if (game.stage11Step == 0) {
                game.mode = Constants.MODE_CINEMATICS;
            }
            //de stage 11 a stage 10
        } else if (game.stage != Constants.STAGE_10 && game.pirate.getTransformedCenter().z < doors[10].getTransformedCenter().z && game.pirate.getTransformedCenter().calcSub(doors[10].getTransformedCenter()).length() < Constants.CACHE_RADIUS) {
            System.out.println("cambio a stage 10");
            game.stage = Constants.STAGE_10;
            removeObjectsFromStage(Constants.STAGE_11);
            addObjectsFromStage(Constants.STAGE_10);
        }
    }

    public synchronized void addObjectsFromStage(int stage) {
        switch (stage) {
            case Constants.STAGE_1:
                //crear switch
                switches[0] = new Switch(game, doors[0]);
                switches[0].translate(0, 0, -68.8f);

                torches[0] = Primitives.getPlane(2, 2.5f);
                torches[0].setTexture("torchtex");
                torches[0].setBillboarding(true);
                torches[0].setTransparency(20);
                torches[0].build();
                torches[0].strip();

                torches[1] = torches[0].cloneObject();
                torches[1].setTexture("torchtex");
                torches[1].clearTranslation();
                torches[1].translate(8f, -2f, 10);

                torches[1] = torches[0].cloneObject();
                torches[1].translate(8f, -2.5f, 0);

                torches[2] = torches[0].cloneObject();
                torches[2].translate(-8f, -2.5f, 50);

                torches[3] = torches[0].cloneObject();
                torches[3].translate(8f, -2.5f, 50);

                torches[4] = torches[0].cloneObject();
                torches[4].translate(-8f, -2.5f, -40);

                torches[5] = torches[0].cloneObject();
                torches[5].translate(8f, -2.5f, -40);

                torches[0].translate(-8f, -2.5f, 0);

                banners[0] = Primitives.getPlane(2, 15f);
                banners[0].setTexture("bannertex");
                banners[0].setBillboarding(true);
                banners[0].setTransparency(20);

                banners[1] = banners[0].cloneObject();
                banners[1].translate(20, -20, 0);

                banners[2] = banners[0].cloneObject();
                banners[2].translate(20, -20, 50);

                banners[3] = banners[0].cloneObject();
                banners[3].translate(-20, -20, 50);

                banners[4] = banners[0].cloneObject();
                banners[4].translate(-20, -20, -50);

                banners[5] = banners[0].cloneObject();
                banners[5].translate(20, -20, -50);

                banners[0].translate(-20, -20f, 0);

                torchesFire[0] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[0].scale(0.5f);
                torchesFire[0].translate(-8f, -5.3f, 0f);
                torchesFire[0].build();

                torchesFire[1] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[1].scale(0.5f);
                torchesFire[1].translate(8f, -5.5f, 0f);
                torchesFire[1].build();

                torchesFire[2] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[2].scale(0.5f);
                torchesFire[2].translate(-8f, -5.5f, 50);
                torchesFire[2].build();

                torchesFire[3] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[3].scale(0.5f);
                torchesFire[3].translate(8f, -5.5f, 50f);
                torchesFire[3].build();

                torchesFire[4] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[4].scale(0.5f);
                torchesFire[4].translate(-8f, -5.5f, -40f);
                torchesFire[4].build();

                torchesFire[5] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[5].scale(0.5f);
                torchesFire[5].translate(8f, -5.5f, -40f);
                torchesFire[5].build();

                torchesFire[6] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[6].scale(0.5f);
                torchesFire[6].translate(-8f, -23.8f, 302.5f);
                torchesFire[6].build();

                torchesFire[7] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[7].scale(0.5f);
                torchesFire[7].translate(8f, -23.8f, 302.5f);
                torchesFire[7].build();

                if (game.stage1Step < 1) {
                    torchesFire[0].setVisibility(false);
                    torchesFire[1].setVisibility(false);
                    torchesFire[2].setVisibility(false);
                    torchesFire[3].setVisibility(false);
                    torchesFire[4].setVisibility(false);
                    torchesFire[5].setVisibility(false);
                    torchesFire[6].setVisibility(false);
                    torchesFire[7].setVisibility(false);
                }

                game.world.addObject(torches[0]);
                game.world.addObject(torches[1]);
                game.world.addObject(torches[2]);
                game.world.addObject(torches[3]);
                game.world.addObject(torches[4]);
                game.world.addObject(torches[5]);

                game.world.addObject(banners[0]);
                game.world.addObject(banners[1]);
                game.world.addObject(banners[2]);
                game.world.addObject(banners[3]);
                game.world.addObject(banners[4]);
                game.world.addObject(banners[5]);

                game.world.addObject(switches[0]);
                game.world.addObject(switches[0].sw);

                break;
            case Constants.STAGE_2:
                break;
            case Constants.STAGE_3:
                //cargar switch 1 scene 3
                switches[1] = new Switch(game, doors[5]);
                switches[1].translate(-88.5f, -18.5f, 263.2f);

                //cargar switch 2 scene 3
                switches[2] = new Switch(game, doors[4]);
                switches[2].translate(-136.9f, -18.5f, 215.6f);

                fftStage3 = new FlyingFireSpreadingThread(Constants.STAGE_3, new SimpleVector(-117, -20, 256));

                try {
                    if (!fftStage3.isAlive()) {
                        fftStage3.start();
                    }
                } catch (Exception e) {
                }

                //agregar elementos al mundo
                game.world.addObject(switches[1]);
                game.world.addObject(switches[1].sw);
                game.world.addObject(switches[2]);
                game.world.addObject(switches[2].sw);
                break;
            case Constants.STAGE_4:
                switches[3] = new Switch(game, doors[6]);
                switches[3].translate(145, -18.5f, 225);

                guillotines[0] = new Guillotine(game);
                guillotines[0].translate(130, -18.5f, 270);
                guillotines[0].rotateY(-(float) Math.PI / 2);

                guillotines[1] = new Guillotine(game);
                guillotines[1].translate(140, -18.5f, 320);
                guillotines[1].rotateY(-(float) Math.PI / 2);

                guillotines[2] = new Guillotine(game);
                guillotines[2].translate(140, -18.5f, 350);
                guillotines[2].rotateY(-(float) Math.PI / 2);

                saws[0] = new Saw(game);
                saws[0].translate(130, -18.5f, 290);
                saws[0].rotateY(-(float) Math.PI / 2);

                saws[1] = new Saw(game);
                saws[1].translate(150, -18.5f, 305);
                saws[1].rotateY(-(float) Math.PI / 2);

                saws[2] = new Saw(game);
                saws[2].translate(140, -18.5f, 335);
                saws[2].rotateY(-(float) Math.PI / 2);

                game.world.addObject(switches[3]);
                game.world.addObject(switches[3].sw);
                game.world.addObject(guillotines[0]);
                game.world.addObject(guillotines[1]);
                game.world.addObject(guillotines[2]);
                game.world.addObject(saws[0]);
                game.world.addObject(saws[1]);
                game.world.addObject(saws[2]);
                break;
            case Constants.STAGE_5:
                bottles[0] = new Bottle(game);
                bottles[0].translate(-35, -29, 335);

                bottles[1] = new Bottle(game);
                bottles[1].translate(35, -29, 335);

                bottles[2] = new Bottle(game);
                bottles[2].translate(-35, -29, 410);

                bottles[3] = new Bottle(game);
                bottles[3].translate(35, -29, 410);

                grims[0] = new Grim(game);
                grims[0].translate(0, -34f, 360);

                torchesFire[0] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[0].scale(0.5f);
                torchesFire[0].translate(22f, -33.3f, 400f);
                torchesFire[0].build();

                torchesFire[1] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[1].scale(0.5f);
                torchesFire[1].translate(-22f, -33.3f, 400f);
                torchesFire[1].build();

                torchesFire[2] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[2].scale(0.5f);
                torchesFire[2].translate(22f, -33.3f, 360f);
                torchesFire[2].build();

                torchesFire[3] = new Effect(game, new SimpleVector(0, 0, 0), Constants.FIRE);
                torchesFire[3].scale(0.5f);
                torchesFire[3].translate(-22f, -33.3f, 360f);
                torchesFire[3].build();

                torches[0] = Primitives.getPlane(2, 2.5f);
                torches[0].setTexture("torchtex");
                torches[0].setBillboarding(true);
                torches[0].setTransparency(20);

                torches[1] = torches[0].cloneObject();
                torches[1].translate(-22f, -30f, 400);

                torches[2] = torches[0].cloneObject();
                torches[2].translate(-22f, -30f, 360);

                torches[3] = torches[0].cloneObject();
                torches[3].translate(22f, -30f, 360);

                torches[0].translate(22f, -30f, 400);

                banners[0] = Primitives.getPlane(2, 20f);
                banners[0].setTexture("bannertex");
                banners[0].setBillboarding(true);
                banners[0].setTransparency(20);

                banners[1] = banners[0].cloneObject();
                banners[1].translate(40, -53, 420);

                banners[2] = banners[0].cloneObject();
                banners[2].translate(40, -53, 340);

                banners[3] = banners[0].cloneObject();
                banners[3].translate(-40, -53, 340);

                banners[0].translate(-40, -53, 420);

                game.world.addObject(bottles[0]);
                game.world.addObject(bottles[1]);
                game.world.addObject(bottles[2]);
                game.world.addObject(bottles[3]);

                game.world.addObject(grims[0]);

                game.world.addObject(torches[0]);
                game.world.addObject(torches[1]);
                game.world.addObject(torches[2]);
                game.world.addObject(torches[3]);

                game.world.addObject(banners[0]);
                game.world.addObject(banners[1]);
                game.world.addObject(banners[2]);
                game.world.addObject(banners[3]);
                break;
            case Constants.STAGE_6:
                bridge = new Bridge(game);

                heads[0] = new Head(game, bridge);
                heads[0].translate(-140.0f, -18.5f, 659.8f);

                heads[1] = new Head(game, doors[5]);
                heads[1].rotateY((float) Math.PI);
                heads[1].translate(-80.0f, -18.5f, 659.8f);

                switches[4] = new Switch(game, doors[8]);
                switches[4].translate(-122.7f, 4.6f, 617.2f);
                switches[4].rotateY((float) Math.PI / 2);

                fftStage6 = new FlyingFireSpreadingThread(Constants.STAGE_6, new SimpleVector(-106, -20, 550));

                try {
                    if (game.stage6Step != 0 && !fftStage6.isAlive()) {
                        fftStage6.start();
                    }
                } catch (Exception e) {
                }

                game.world.addObject(bridge);
                game.world.addObject(heads[0]);
                game.world.addObject(heads[1]);
                game.world.addObject(switches[4]);
                game.world.addObject(switches[4].sw);
                break;
            case Constants.STAGE_7:
                grims[0] = new Grim(game);
                grims[0].translate(-105.0f, -1.5f, 749.5f);

                game.world.addObject(grims[0]);
                if (game.stage7Step > 0) {
                    grims[0].setVisibility(false);
                }
                break;
            case Constants.STAGE_8:

                boxes[0] = new Box(game, true);
                boxes[0].translate(172.5f, -18.5f, 410);

                boxes[1] = new Box(game, true);
                boxes[1].translate(88.5f, -18.5f, 500);

                boxes[2] = new Box(game, true);
                boxes[2].translate(82f, -21.5f, 415);

                saws[0] = new Saw(game);
                saws[0].translate(130, -18.5f, 450);

                saws[1] = new Saw(game);
                saws[1].translate(120, -18.5f, 460);

                saws[2] = new Saw(game);
                saws[2].translate(140, -18.5f, 470);
                //guillotines[0].rotateY(-(float)Math.PI/2);

                guillotines[0] = new Guillotine(game);
                guillotines[0].translate(160, -18.5f, 520);
                guillotines[0].rotateY(-(float) Math.PI / 2);

                guillotines[1] = new Guillotine(game);
                guillotines[1].translate(160, -18.5f, 510);
                guillotines[1].rotateY(-(float) Math.PI / 2);

                game.world.addObject(boxes[0]);
                game.world.addObject(boxes[1]);
                game.world.addObject(boxes[2]);
                game.world.addObject(saws[0]);
                game.world.addObject(saws[1]);
                game.world.addObject(saws[2]);
                game.world.addObject(guillotines[0]);
                game.world.addObject(guillotines[1]);
                break;
            case Constants.STAGE_9:
                grims[0] = new Grim(game);
                grims[0].translate(137.4f, -24.0f, 585.4f);

                game.world.addObject(grims[0]);
                if (game.stage9Step > 0) {
                    grims[0].setVisibility(false);
                }
                break;
            case Constants.STAGE_11:
                devil = new Devil(game);
                devil.translate(2f, -40f, 643f);
                devil.rotateY((float) Math.PI);

                game.world.addObject(devil);
                break;
        }
        MemoryHelper.compact();
    }

    public void actualizeDoors(int stage) {
        for (int i = 0; i < doors.length; i++)
            doors[i].setVisibility(false);
        switch (stage) {
            case Constants.STAGE_1:
                doors[0].setVisibility(true);
                break;
            case Constants.STAGE_2:
                doors[0].setVisibility(true);
                doors[1].setVisibility(true);
                doors[2].setVisibility(true);
                doors[3].setVisibility(true);
                break;
            case Constants.STAGE_3:
                doors[1].setVisibility(true);
                doors[4].setVisibility(true);
                doors[5].setVisibility(true);
                break;
            case Constants.STAGE_4:
                doors[2].setVisibility(true);
                doors[6].setVisibility(true);
                break;
            case Constants.STAGE_5:
                doors[3].setVisibility(true);
                doors[9].setVisibility(true);
                break;
            case Constants.STAGE_6:
                doors[5].setVisibility(true);
                doors[8].setVisibility(true);
                break;
            case Constants.STAGE_7:
                doors[8].setVisibility(true);
                break;
            case Constants.STAGE_8:
                doors[6].setVisibility(true);
                doors[7].setVisibility(true);
                break;
            case Constants.STAGE_9:
                doors[7].setVisibility(true);
                break;
            case Constants.STAGE_10:
                doors[9].setVisibility(true);
                doors[10].setVisibility(true);
                break;
            case Constants.STAGE_11:
                doors[10].setVisibility(true);
                break;
        }

    }

    public void removeObjectsFromStage(int stage) {
        switch (stage) {
            case Constants.STAGE_1:
                //remover elementos del mundo
                game.world.removeObject(switches[0]);
                game.world.removeObject(switches[0].sw);
                game.world.removeObject(torches[0]);
                game.world.removeObject(torches[1]);
                game.world.removeObject(torches[2]);
                game.world.removeObject(torches[3]);
                game.world.removeObject(torches[4]);
                game.world.removeObject(torches[5]);

                game.world.removeObject(banners[0]);
                game.world.removeObject(banners[1]);
                game.world.removeObject(banners[2]);
                game.world.removeObject(banners[3]);
                game.world.removeObject(banners[4]);
                game.world.removeObject(banners[5]);

                for (Effect tf : torchesFire) {
                    tf.a.state = 3;
                    game.world.removeObject(tf);
                }

                //eliminar elementos
                switches[0] = null;
                torches[0] = null;
                torches[1] = null;
                torches[2] = null;
                torches[3] = null;
                torches[4] = null;
                torches[5] = null;

                banners[0] = null;
                banners[1] = null;
                banners[2] = null;
                banners[3] = null;
                banners[4] = null;
                banners[5] = null;

                torchesFire[0] = null;
                torchesFire[1] = null;
                torchesFire[2] = null;
                torchesFire[3] = null;
                torchesFire[4] = null;
                torchesFire[5] = null;
                torchesFire[6] = null;
                torchesFire[7] = null;

                break;
            case Constants.STAGE_2:
                break;
            case Constants.STAGE_3:

                //remover elementos del mundo
                game.world.removeObject(switches[1]);
                game.world.removeObject(switches[1].sw);
                game.world.removeObject(switches[2]);
                game.world.removeObject(switches[2].sw);

                for (FlyingFire ff : ff3) {
                    if (ff != null) {
                        ff.a.state = 3;
                        game.world.removeObject(ff);
                    }
                }

                fftStage3.state = 3;
                fftStage3.fireNumber = 0;

                //eliminar elementos
                switches[1] = null;
                switches[2] = null;
                for (int i = 0; i < ff3.length; i++) {
                    ff3[i] = null;
                }
                break;
            case Constants.STAGE_4:

                game.world.removeObject(switches[3]);
                game.world.removeObject(switches[3].sw);
                game.world.removeObject(guillotines[0]);
                game.world.removeObject(guillotines[1]);
                game.world.removeObject(guillotines[2]);
                game.world.removeObject(saws[0]);
                game.world.removeObject(saws[1]);
                game.world.removeObject(saws[2]);

                guillotines[0] = null;
                guillotines[1] = null;
                guillotines[2] = null;
                saws[0] = null;
                saws[1] = null;
                saws[2] = null;
                switches[3] = null;
                break;
            case Constants.STAGE_5:
                game.world.removeObject(bottles[0]);
                game.world.removeObject(bottles[1]);
                game.world.removeObject(bottles[2]);
                game.world.removeObject(bottles[3]);

                game.world.removeObject(grims[0]);

                game.world.removeObject(torchesFire[0]);
                game.world.removeObject(torchesFire[1]);
                game.world.removeObject(torchesFire[2]);
                game.world.removeObject(torchesFire[3]);

                game.world.removeObject(torches[0]);
                game.world.removeObject(torches[1]);
                game.world.removeObject(torches[2]);
                game.world.removeObject(torches[3]);

                game.world.removeObject(banners[0]);
                game.world.removeObject(banners[1]);
                game.world.removeObject(banners[2]);
                game.world.removeObject(banners[3]);

                bottles[0] = null;
                bottles[1] = null;
                bottles[2] = null;
                bottles[3] = null;

                grims[0] = null;

                torchesFire[0] = null;
                torchesFire[1] = null;
                torchesFire[2] = null;
                torchesFire[3] = null;

                torches[0] = null;
                torches[1] = null;
                torches[2] = null;
                torches[3] = null;

                banners[0] = null;
                banners[1] = null;
                banners[2] = null;
                banners[3] = null;
                break;
            case Constants.STAGE_6:
                game.world.removeObject(bridge);
                game.world.removeObject(heads[0]);
                game.world.removeObject(heads[1]);
                game.world.removeObject(switches[4]);
                game.world.removeObject(switches[4].sw);

                heads[0].a.state = 3;
                heads[1].a.state = 3;

                for (FlyingFire ff : ff6) {
                    if (ff != null) {
                        ff.a.state = 3;
                        game.world.removeObject(ff);
                    }
                }

                fftStage6.state = 3;
                fftStage6.fireNumber = 0;

                bridge = null;
                heads[0] = null;
                heads[1] = null;
                switches[4] = null;
                for (int i = 0; i < ff6.length; i++) {
                    ff6[i] = null;
                }
                break;
            case Constants.STAGE_7:
                game.world.removeObject(game.castle.grims[0]);
                grims[0] = null;
                if (knight != null) {
                    knight.a.state = 3;
                    knight.gravity.state = 3;
                    game.world.removeObject(knight);
                    knight = null;
                }
                break;
            case Constants.STAGE_8:
                game.world.removeObject(boxes[0]);
                game.world.removeObject(boxes[1]);
                game.world.removeObject(boxes[2]);
                game.world.removeObject(saws[0]);
                game.world.removeObject(saws[1]);
                game.world.removeObject(saws[2]);
                game.world.removeObject(guillotines[0]);
                game.world.removeObject(guillotines[1]);

                boxes[0] = null;
                boxes[1] = null;
                boxes[2] = null;
                saws[0] = null;
                saws[1] = null;
                saws[2] = null;
                guillotines[0] = null;
                guillotines[1] = null;
                break;
            case Constants.STAGE_9:
                game.world.removeObject(grims[0]);
                grims[0] = null;
                if (knight != null) {
                    game.world.removeObject(knight);
                    knight.a.state = 3;
                    knight.gravity.state = 3;
                    knight = null;
                }
                break;
            case Constants.STAGE_11:
                if (devil != null) {
                    game.world.removeObject(devil);
                    devil.a.state = 3;
                    devil.a2.state = 3;
                    devil.gravity.state = 3;
                    devil = null;
                }
                break;
        }
        MemoryHelper.compact();
    }

    public class FlyingFireSpreadingThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        public int fireNumber;
        public int fireStage;
        public SimpleVector pos;
        public boolean spreading;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 5000;
        private long beforeTime;
        private int state = 1;

        public FlyingFireSpreadingThread(int stage, SimpleVector pos) {
            fireStage = stage;
            this.pos = pos;
        }

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (this) {
                    if (fireNumber < Constants.FIRE_NUMBER) {
                        if (fireStage == Constants.STAGE_3) {
                            ff3[fireNumber] = new FlyingFire(game, pos, fireStage);
                        } else if (fireStage == Constants.STAGE_6) {
                            ff6[fireNumber] = new FlyingFire(game, pos, fireStage);
                        }
                        fireNumber++;
                    }
                }

                System.out.println("flying fire");

                this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);
                try {
                    //actual sleep code
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException ex) {
                }

                if (game.stop)
                    state = STOPED;
            }
            if (state == STOPED) {
                System.out.println("finalize thread");
                try {
                    this.finalize();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public class SceneSwitchThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 500;
        private long beforeTime;
        private int state = 1;

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                synchronized (game) {
                    try {
                        loadCache();
                    } catch (Exception e) {
                        Logger.log("Load Cache error!", Logger.MESSAGE);
                    }
                }

                this.sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);
                try {
                    //actual sleep code
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException ex) {
                }

                if (game.stop)
                    state = STOPED;
            }
            if (state == STOPED) {
                System.out.println("finalize thread");
                try {
                    this.finalize();
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
