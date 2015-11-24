package micromobil.eltesorodelmar.physic;

import android.media.MediaPlayer;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import java.util.Random;

import micromobil.eltesorodelmar.GL.Render;
import micromobil.eltesorodelmar.R;
import micromobil.eltesorodelmar.activity.MainGame;
import micromobil.eltesorodelmar.object3D.Cross;
import micromobil.eltesorodelmar.object3D.Effect;
import micromobil.eltesorodelmar.object3D.Knight;
import micromobil.eltesorodelmar.util.Constants;
import micromobil.eltesorodelmar.util.TextBlitter;

public class Cinematics {

    public static TextBlitter tb;
    public static MediaPlayer battlesong;
    public static MediaPlayer endingsong;
    public Render game;
    public QuakeThread a;
    public String[] scene1 = new String[31];
    public String[] scene1_extra = new String[2];
    public String[] scene5 = new String[9];
    public String[] scene6 = new String[8];
    public String[] scene7 = new String[4];
    public String[] scene9 = new String[4];
    public String[] scene11 = new String[32];

    public String[] battle1 = new String[11];
    public String[] battle2 = new String[7];
    public String[] battle1End = new String[3];
    public String[] battle2End = new String[3];
    public String[] battle;

    public String gaviota = "Gaviota:\n";
    public String pirata = "Pirata:\n";
    public String william = "William:\n";

    public Camera stage1Cam;
    public Camera stage6Cam;
    public Camera stage11Cam;

    public int index;
    public int lights;
    public boolean ready;

    public boolean stage7End;
    public boolean stage9End;

    public int steps;

    public StringBuffer sceneBuffer = new StringBuffer();

    public Cinematics(Render game) {
        tb = new TextBlitter();
        this.game = game;

        a = new QuakeThread();

        scene1[0] = gaviota + "Hola forastero.\nHace tiempo que no veo a alguien\nen estas tierras.";
        scene1[1] = pirata + "��Qui�n eres?!";
        scene1[2] = gaviota + "Soy una gaviota imb�cil jajaja.";
        scene1[3] = pirata + "Cabr�n.";
        scene1[4] = gaviota + "�C�mo has llegado hasta ac�\nforastero?";
        scene1[5] = pirata + "S�lo quiero saber una cosa\nmaldito, �esta es la\nlegendaria isla errante?";
        scene1[6] = gaviota + "As� es.";
        scene1[7] = pirata + "�Estoy en la gloria jajaja!";
        scene1[8] = pirata + "A un lado basura, tengo trabajo\nque hacer.";
        scene1[9] = gaviota + "Jajaja. �Cu�l es tu nombre\nforastero?";
        scene1[10] = pirata + "Jeje, soy el famoso cocodrilo de\nlos mares, William El\nDesquiciado.";
        scene1[11] = gaviota + "Wow, que nombre m�s rid�culo.";
        scene1[12] = william + "Calla basura. �Y t� qu�\nsabes de este castillo?";
        scene1[13] = gaviota + "He vivido muchos a�os ac�,\nconosco bastante sobre\nlos peligros que existen en\neste cruel castillo.";
        scene1[14] = william + "Bien pajarraco, ayudame a\nsacar cosas de ac�.";
        scene1[15] = gaviota + "Mmm.. est� bien, te acompa�ar�,\nquiero ver hasta donde puedes\nllegar antes de que el mar hable.";
        scene1[16] = william + "�Ah?";
        scene1[17] = gaviota + "Ahora todos pertenecemos al mar.";
        scene1[18] = william + "T� si idiota, eres una gaviota.";
        scene1[19] = gaviota + "Jejeje eso ya lo s�.";
        scene1[20] = william + "Basta de estupideces, ayudame a\nencontrar los malditos tesoros.";
        scene1[21] = william + "Arrgg, por qu� est� tan oscuro,\ncasi no veo por donde voy.";
        scene1[22] = gaviota + "Arreglemos eso.";
        scene1[23] = "";
        scene1[24] = william + "��C�mo hiciste eso?!";
        scene1[25] = gaviota + "Es un truco para impresionar\na los que llegan ac�.";
        scene1[26] = william + "Excelente. �Y tienes alg�n\ntruco para poner algo de\nm�sica?";
        scene1[27] = gaviota + "Hehehe, si puedo hacer eso.";
        scene1[28] = "";

        scene1_extra[0] = william + "��C�mo abro esto?!";
        scene1_extra[1] = gaviota + "Activa el switch idiota,\ncomo en todos los juegos de este\nestilo.";

        scene5[0] = gaviota + "Esta al parecer es la bit�cora\nde alg�n investigador. Aqu�\nse relata la historia de esta\nisla.";
        scene5[1] = " \"La isla en un principio fue\nel trono de un poderoso rey hace\nmiles de a�os, hasta que un\nterrible terremoto dividi� su\nreino y su castillo se separ� de\nla tierra, convirti�ndose en un\ntrozo de tierra suelto que\ndeambul� por los mares.";
        scene5[2] = " La isla perdida fue a parar a\nlas orillas de otro poderoso\nreino, donde su rey asombrado de\nlas maravillas del castillo\ndecidi� convertir esa isla en\nsu nuevo trono, embelleciendo\nel castillo con su riqueza.\"";
        scene5[3] = " ................";
        scene5[4] = " ................";
        scene5[5] = gaviota + "Y no dice nada m�s.";
        scene5[6] = william + "Que p�rdida de tiempo. T� si\nsabes la historia completa de\nesta isla pajarraco, �cierto?.";
        scene5[7] = gaviota + "Lo he visto con mis propios\nojos, pero tranquilo, que tu\ntambi�n lo ver�s.";
        scene5[8] = william + "Por supuesto, obtendr� todos\nlos tesoros de este maldito\ncastillo.";

        scene6[0] = william + "��Por qu� la puerta se ha\ncerrado?!";
        scene6[1] = gaviota + "Nos han atrapado.";
        scene6[2] = william + "��Qui�nes?!";
        scene6[3] = gaviota + "Esp�ritus que viven en el\ncastillo.";
        scene6[4] = william + "Maldici�n, este castillo me\nest� comenzando a fastidiar.";
        scene6[5] = gaviota + "Encuentra la salida.";
        scene6[6] = william + "�Y c�mo hago eso, gusano?";
        scene6[7] = gaviota + "No tengo idea jejeje.";

        scene7[0] = gaviota + "Leer� lo que dice aqu�.";
        scene7[1] = " \"31 de Julio de 1197:\nJuro por siempre servir a la\ncorona y a sus posesiones,\naun despu�s de la muerte.";
        scene7[2] = " Juro resguardar esta\nhabitaci�n por siempre, proteger\nel mayor secreto del reino\nde Inglaterra.";
        scene7[3] = " �Nunca descansar, por\nel rey!\"";

        scene9[0] = gaviota + "Leer� lo que dice aqu�.";
        scene9[1] = " \"26 de Mayo de 993:\nJuro por siempre proteger el\nmayor tesoro del Sacro Imperio\nRomano Germ�nico.";
        scene9[2] = " Que ni la muerte acabe\ncon el poder y legado de los\npueblos del imperio, heredero\ndel Imperio Romano.";
        scene9[3] = " �Muerte a los enemigos de\nla corona!\"";

        battle1End[0] = gaviota + "Excelente Knock-out.";
        battle1End[1] = william + "Jejeje, lo dej� helado.";
        battle1End[2] = gaviota + "Ahora, por el otro tesoro.";

        battle2End[0] = william + "Jajaja, �te venc� idiota!";
        battle2End[1] = gaviota + "Bien hecho, ahora podr�s\nentrar a la habitaci�n\nprincipal del castillo.";
        battle2End[2] = william + "Excelente, este castillo\nya me tiene muy fastidiado.";

        battle1[4] = william + "�Qu� es esta porquer�a?";
        battle1[5] = gaviota + "No estamos solos.";
        battle1[6] = william + "�Oh dios!";
        battle1[7] = gaviota + "�Est�s asustado ni�ato?";
        battle1[8] = william + "Jajaja, �por fin un reto de\nverdad!";
        battle1[9] = gaviota + "Jajaja, payaso. Quien vive\nac� est� muerto. Protege el\ntesoro que hay en esta\nhabitaci�n aun despu�s\nde la muerte.";
        battle1[10] = william + "Lo dejar� m�s muerto a�n,\njejeje. �Sal de una vez\nbasura!";

        battle2[4] = william + "Creo que ya s� qu� es lo que\nsigue...";
        battle2[5] = gaviota + "Exacto, pero con este segundo\ntesoro podr�s acceder a la\nhabitaci�n principal del\ncastillo.";
        battle2[6] = william + "Interesante.";

        scene11[0] = william + "�Noooo! �Nuevamente estoy\natrapado! �Arrgg!";
        scene11[1] = gaviota + "No te preocupes, que pronto\nestar�s muerto.";
        scene11[2] = william + "��C�mo has dicho insecto?!";
        scene11[3] = gaviota + "As� es esta isla, �mortal!";
        scene11[4] = william + "Idiota, he pasado por muchos\nobstaculos mortales sin\ndespeinarme, �c�mo podr�a\nmorir ahora?";
        scene11[5] = gaviota + "Prep�rate.";
        scene11[6] = william + "Jejeje siempre lo estoy.";
        scene11[7] = william + "�Qu� es esto, otro enemigo?";
        scene11[8] = gaviota + "Era uno, pero ahora est� de\npiedra.";
        scene11[9] = william + "Por suerte, no quiero m�s\nproblemas.";
        scene11[10] = gaviota + "Pero quiz�s pueda revivir.";
        scene11[11] = william + "��C�mo?!";
        scene11[12] = william + "Jajaja.. jaja, he...\nvencido...";
        scene11[13] = gaviota + "Felicitaciones\nbastardo.";
        scene11[14] = william + "�Te dije que no iba a\nmorir!";
        scene11[15] = gaviota + "No morir�s asesinado por\nmonstruos. Morir�s por\ncausas naturales jejeje.";
        scene11[16] = william + "�C�mo podr�a pasar eso idiota?";
        scene11[17] = gaviota + "Esta isla esconde un secreto\nmortal.";
        scene11[18] = gaviota + "Yo tengo el resto de la\nbit�cora del investigador,\nla ocult� para que los visitantes\nde esta isla no escaparan y\nse unan a nosotros.";
        scene11[19] = william + "�Habla idiota, antes de que\nte corte en dos!";
        scene11[20] = gaviota + "Jejeje, te la dir�.";
        scene11[21] = " \"Un d�a la isla comenz�\nnuevamente su marcha con\ntodos sus habitantes adentro,\nhasta parar en un misterioso\ntorbellino en medio del oc�ano.";
        scene11[22] = " La isla fue tragada, pero\nincre�blemente el agua no\ntocaba su interior, puesto que\npor extra�as razones f�sicas\nel torbellino mantuvo a la isla\nen su centro, as� como en una\nespecie de burbuja dentro de s�.";
        scene11[23] = " As� la isla se mantuvo intacta\nmientras sus habitantes\nhallaban la muerte por asfixia.";
        scene11[24] = " Despu�s de algunos siglos esta\nisla logra escapar del torbellino\npara seguir con su marcha y\nrepetir la historia nuevamente.\nDistintos reinos y eras, pero\nsiempre la codicia como\nprotagonista.";
        scene11[25] = " Reyes agrandaron cada vez m�s\neste castillo y lo repletaron de\ntesoros y con trampas lo\nhicieron impenetrable, s�lo para\nencontrar la muerte tiempo\ndespu�s.";
        scene11[26] = " Sus almas no descansan en paz\npor lo que sus guerreros cuidan\nel palacio a�n despu�s de\nmuertos. Dentro se contienen los\nm�s grandes tesoros y misterios\nde la humanidad, quien sabe\nqu� puede haber en lo m�s\nprofundo de este.";
        scene11[27] = " Esta historia se repiti� por\nmuchos siglos... �y sigue\nrepiti�ndose!\"";
        scene11[28] = gaviota + "�Esta isla es el tesoro del mar,\ny nosotros somos sus hijos!";
        scene11[29] = gaviota + "Y es hora de volver,\nvolver a casa.";
        scene11[30] = gaviota + "Bienvenido pirata William.\nMientras estabas ac� la isla\nse mov�a hacia el torbellino\nque la regresar� a donde\npertenece.";
        scene11[31] = gaviota + "Est� comenzando, volvemos\na casa...";
    }

    public void sceneCinematics(FrameBuffer fb, int stage) {
        switch (stage) {
            case Constants.STAGE_1:
                scene1(fb);
                break;
            case Constants.STAGE_5:
                scene5(fb);
                break;
            case Constants.STAGE_6:
                scene6(fb);
                break;
            case Constants.STAGE_7:
                scene7(fb);
                break;
            case Constants.STAGE_9:
                scene9(fb);
                break;
            case Constants.STAGE_11:
                scene11(fb);
                break;
        }
    }

    public void scene1(FrameBuffer fb) {
        if (game.stage1Step == 0) {
            if (index == 0) {
                stage1Cam = game.world.getCamera();
            }
            if (index < 23) {
                textWriter(fb, scene1[index]); //escribe los di�logos
            } else if (index == 23) {
                if (lights == 0) {
                    Camera lCam = new Camera();
                    lCam.setPosition(-10, -5, -10);
                    lCam.lookAt(game.gull.getTransformedCenter());
                    game.world.setCameraTo(lCam);
                }
                if (lights < 10) {
                    new Effect(game, game.gull.getTransformedCenter(), Constants.PARTICLES); //agrega una chispa
                } else if (lights == 150) {
                    game.lamp.enable(); //ilumina la habitaci�n
                    game.world.setFogging(World.FOGGING_DISABLED); //deshabilita la niebla
                    game.castle.torchesFire[0].setVisibility(true);
                    game.castle.torchesFire[1].setVisibility(true);
                    game.castle.torchesFire[2].setVisibility(true);
                    game.castle.torchesFire[3].setVisibility(true);
                    game.castle.torchesFire[4].setVisibility(true);
                    game.castle.torchesFire[5].setVisibility(true);
                } else if (lights == 300) {
                    game.world.setCameraTo(stage1Cam);
                    index++;
                }
                lights++;
            } else if (index > 23 && index < 28) {
                textWriter(fb, scene1[index]); //escribe los di�logos
            } else if (index == 28) {
                game.mode = Constants.MODE_NORMAL;
                MainGame.mp.start();
                game.world.setCameraTo(game.cam);
                game.gull.clearTranslation();
                game.gull.clearRotation();
                game.gull.translate(0, -1, 0);
                game.gull.addParent(game.pirate);
                game.stage1Step++;
                index = 0;
                //fin del di�logo con la gaviota
            }
        } else {
            if (index >= 0 && index < 2) { //el pirata toca la puerta cerrada
                textWriter(fb, scene1_extra[index]);
            } else if (index == 2) {
                game.mode = Constants.MODE_NORMAL;
                game.stage1Step++;
                index = 0;
            }
        }
    }

    public void scene5(FrameBuffer fb) {
        if (index >= 0 && index < scene5.length) {
            textWriter(fb, scene5[index]);
        } else if (index == scene5.length) {
            game.mode = Constants.MODE_NORMAL;
            game.world.setCameraTo(game.cam);
            //se elimina del mundo al grim
            game.castle.grims[0].setVisibility(false);
            index = 0;
        }
    }

    public void scene6(FrameBuffer fb) {
        if (steps == 0) {
            //la c�mara se posiciona en la posici�n de la puerta
            stage6Cam = new Camera();
            stage6Cam.setPositionToCenter(game.castle.doors[5]);
            stage6Cam.setOrientation(new SimpleVector(0, 0, 1), new SimpleVector(0, -1, 0));
            game.world.setCameraTo(stage6Cam);
            game.pirate.setOrientation(new SimpleVector(0, 0, 1), new SimpleVector(0, 1, 0));
            game.pirate.state = Constants.WALK;
            steps++;
        }
        if (steps < 70) {
            //se hace caminar al pirata unos cuantos pasos autom�ticamente
            game.pirate.translate(0, 0, 0.25f);
            steps++;
        } else if (steps == 70) {
            //la c�mara mira a la puerta
            game.pirate.state = Constants.STAND;
            stage6Cam.setPositionToCenter(game.pirate);
            game.world.setCameraTo(stage6Cam);
            game.world.checkCameraCollisionEllipsoid(Camera.CAMERA_MOVEIN, Constants.CAM_ELLIPSOID, 30, Constants.RECURSION);
            stage6Cam.moveCamera(Camera.CAMERA_MOVEDOWN, 5);
            stage6Cam.lookAt(game.castle.doors[5].getTransformedCenter());
            game.castle.doors[5].deactivate();
            steps++;
        } else if (steps > 70 && steps <= 71) {
            //la puerta se cierra
            if (game.castle.doors[5].state == Constants.DOOR_STAND) {
                game.world.setCameraTo(game.cam);
                game.pirate.moveCameraBehind();
                steps++;
            }
        } else if (steps == 72) {
            steps++;
        } else if (steps == 73) {
            //comienza el di�logo
            if (index < 8) {
                textWriter(fb, scene6[index]);
            } else if (index == 8) {
                game.mode = Constants.MODE_NORMAL;
                game.stage6Step++;
                game.castle.fftStage6.start(); //se inicia el fuego volador de la scene
                index = 0;
                steps = 0;
            }
        }
    }

    public void scene7(FrameBuffer fb) {
        if (stage7End == false) {
            if (game.crosses == 0) {
                battle = battle1;
            } else {
                battle = battle2;
            }
            if (index >= 4 && index < battle.length) {
                textWriter(fb, battle[index]);
            } else if (index == battle.length) {
                game.castle.doors[8].setDoorCam(1);
                game.castle.doors[8].deactivate();
                index++;
            } else if (index == 0 || index < 4) {
                textWriter(fb, scene7[index]);
                //index++;
            } else if (index == battle.length + 1) {
                if (game.castle.doors[8].state == Constants.DOOR_STAND) {
                    game.mode = Constants.MODE_NORMAL;
                    game.world.setCameraTo(game.cam);
                    //se elimina del mundo al grim
                    game.castle.grims[0].setVisibility(false);
                    //se crea un nuevo caballero
                    game.castle.knight = new Knight(game);
                    game.castle.knight.translate(-106.4f, -2f, 699.5f);
                    stage7End = true;
                    MainGame.mp.pause();
                    battlesong = MediaPlayer.create(game.context, R.raw.battle);
                    battlesong.setLooping(true);
                    battlesong.start();
                    index = 0;
                }
            }
        } else {
            if (game.crosses == 0) {
                battle = battle1End;
            } else {
                battle = battle2End;
            }
            if (index >= 0 && index <= 2) {
                textWriter(fb, battle[index]);
            } else if (index == 3) {
                for (int i = 0; i < 5; i++) {
                    new Effect(game, game.castle.knight.getTransformedCenter(), Constants.PARTICLES);
                }
                Cross cross = new Cross(game);
                cross.translate(game.castle.knight.getTransformedCenter());
                game.world.addObject(cross);
                game.mode = Constants.MODE_NORMAL;
                game.world.setCameraTo(game.cam);
                index = 0;

                //eliminar al caballero del mundo
                game.world.removeObject(game.castle.knight);
                game.castle.knight.a.state = 3;
                game.castle.knight.gravity.state = 3;
                game.castle.knight = null;

                battlesong.stop();
                //battlesong.release();
                MainGame.mp.start();
            }
        }
    }

    public void scene9(FrameBuffer fb) {
        if (stage9End == false) {
            if (game.crosses == 0) {
                battle = battle1;
            } else {
                battle = battle2;
            }
            if (index >= 4 && index < battle.length) {
                textWriter(fb, battle[index]);
            } else if (index == battle.length) {
                game.castle.doors[7].setDoorCam(1);
                game.castle.doors[7].deactivate();
                index++;
            } else if (index == 0 || index < 4) {
                textWriter(fb, scene9[index]);
                //index++;
            } else if (index == battle.length + 1) {
                if (game.castle.doors[7].state == Constants.DOOR_STAND) {
                    game.mode = Constants.MODE_NORMAL;
                    game.world.setCameraTo(game.cam);
                    //se elimina del mundo al grim
                    game.castle.grims[0].setVisibility(false);
                    //se crea un nuevo caballero
                    game.castle.knight = new Knight(game);
                    game.castle.knight.translate(137.2f, -24f, 530f);
                    stage9End = true;
                    MainGame.mp.pause();
                    battlesong = MediaPlayer.create(game.context, R.raw.battle);
                    battlesong.setLooping(true);
                    battlesong.start();
                    index = 0;
                }
            }
        } else {
            if (game.crosses == 0) {
                battle = battle1End;
            } else {
                battle = battle2End;
            }
            if (index >= 0 && index <= 2) {
                textWriter(fb, battle[index]);
            } else if (index == 3) {
                for (int i = 0; i < 5; i++) {
                    new Effect(game, game.castle.knight.getTransformedCenter(), Constants.PARTICLES);
                }
                game.world.removeObject(game.castle.knight);
                Cross cross = new Cross(game);
                cross.translate(game.castle.knight.getTransformedCenter());
                game.world.addObject(cross);
                game.mode = Constants.MODE_NORMAL;
                game.world.setCameraTo(game.cam);
                index = 0;
                game.castle.knight = null;

                battlesong.stop();
                //battlesong.release();
                MainGame.mp.start();
            }
        }
    }

    public void scene11(FrameBuffer fb) {
        if (steps == 0) {
            //la c�mara se posiciona en la posici�n de la puerta
            stage11Cam = new Camera();
            stage11Cam.setPositionToCenter(game.castle.doors[10]);
            stage11Cam.setOrientation(new SimpleVector(0, 0, 1), new SimpleVector(0, -1, 0));
            stage11Cam.moveCamera(Camera.CAMERA_MOVEIN, 40);
            stage11Cam.moveCamera(Camera.CAMERA_MOVERIGHT, 20);
            stage11Cam.lookAt(game.castle.doors[10].getTransformedCenter());
            game.world.setCameraTo(stage11Cam);
            game.pirate.setOrientation(new SimpleVector(0, 0, 1), new SimpleVector(0, 1, 0));
            game.pirate.state = Constants.WALK;
            steps++;
        }
        if (steps < 50) {
            //se hace caminar al pirata unos cuantos pasos autom�ticamente
            game.pirate.translate(0, 0, 0.25f);
            steps++;
        } else if (steps == 50) {
            //la c�mara mira a la puerta
            game.pirate.state = Constants.STAND;
            game.castle.doors[10].setDoorCam(1);
            game.castle.doors[10].deactivate();
            steps++;
        } else if (steps > 50 && steps <= 51) {
            //la puerta se cierra
            if (game.castle.doors[10].state == Constants.DOOR_STAND) {
                game.world.setCameraTo(stage11Cam);
                steps++;
            }
        } else if (steps == 52) {
            steps++;
        } else if (steps == 53) {
            //comienza el di�logo
            if (index < 7) {
                textWriter(fb, scene11[index]);
            } else if (index == 7) {
                game.mode = Constants.MODE_NORMAL;
                game.stage11Step++;
                game.world.setCameraTo(game.cam);
                game.pirate.moveCameraBehind();
                steps++;
            }
            //el pirata habla con el jefe final
        } else if (steps == 54) {
            game.pirate.state = Constants.STAND;
            //comienza el di�logo
            if (index >= 7 && index < 12) {
                textWriter(fb, scene11[index]);
            } else if (index == 12) {
                game.mode = Constants.MODE_NORMAL;
                game.stage11Step++;
                game.world.setCameraTo(game.cam);
                game.pirate.moveCameraBehind();
                game.castle.devil.a.start();
                game.castle.devil.a2.start();
                game.castle.devil.mode = 0;
                MainGame.mp.stop();
                battlesong = MediaPlayer.create(game.context, R.raw.battle);
                battlesong.setLooping(true);
                battlesong.start();
                steps++;
            }
            //has vencido al jefe final
        } else if (steps == 55) {
            battlesong.stop();
            //battlesong.release();
            steps++;
        } else if (steps == 56) {
            if (index >= 12 && index < 31) {
                textWriter(fb, scene11[index]);
            } else {
                a.start();
                steps++;
            }
            //control de la m�sica
            if (index == 17) {
                if (endingsong == null) {
                    endingsong = MediaPlayer.create(game.context, R.raw.drag);
                    endingsong.setLooping(true);
                    endingsong.start();
                }
            }
        } else if (steps > 56 && steps < 256) {
            steps++;
        } else if (steps == 256) {
            if (index == 31) {
                textWriter(fb, scene11[index]);
            } else {
                steps++;
            }
        } else if (steps == 257) {
            a.state = 3;
            //Cr�ditos
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ((MainGame) game.context).credits();
        }
    }

    //M�todo que escribe los di�logos
    public void textWriter(FrameBuffer fb, String scene) {
        if (sceneBuffer.length() < scene.length()) {
            char c = scene.charAt(sceneBuffer.length());
            sceneBuffer.append(c);
        } else {
            ready = true;
        }
        tb.blitText(fb, sceneBuffer.toString(), 100, 100);
    }

    public class QuakeThread extends Thread {
        public final static int RUNNING = 1;
        public final static int PAUSED = 2;
        public final static int STOPED = 3;
        private long sleepTime;
        //amount of time to sleep for (in milliseconds)
        private long delay = 2000;
        private long beforeTime;
        private int state = 1;

        @Override
        public void run() {
            while (state == RUNNING) {
                beforeTime = System.nanoTime();
                Random random = new Random();
                synchronized (game) {
                    game.vibrator.vibrate(random.nextInt(300));
                }

                delay = random.nextInt(2000);
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
