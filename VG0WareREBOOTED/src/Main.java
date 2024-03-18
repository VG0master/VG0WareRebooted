import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    //SETTINGS:

    //DONT TURN OFF NEEDS TO BE IMPLEMENTED CORRECTLY
    private static boolean allShinies = true;


    //Only if the move in your first slot is 100% acc and can kill all tanorians in hoard 100%
    private static boolean killHoards = true; //With one move
    private static String chainTarget = "Stagurai";

    //private static Object[][] targets = {{"Fordrake",true}};


    //END
    private static Robot r;

    private static Tanorians tanorians;

    private static int keyCode = KeyEvent.VK_D;

    private static boolean isHoard = false;
    private static long totalOffset = 0;

    private static Tanorian currentTanorian;
    private static int totalEncounters;
    private static boolean gameRunning = false;

    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Starting");
        parseJson("data.json");
        gameRunning = true;
        while (gameRunning) {
            betterStrafe(1000); //Get into encounter
            waitForColorAtPosition(new Point(2285,1306), new Color(255,255,255)); //The white part of the RUN text in encounter.
            BufferedImage image = r.createScreenCapture(new Rectangle(2167,122,191,42)); //The nametag of tanorian (NEEDS TO BE COMPLETELY INSIDE, NO TERRAIN ONLY GUI)
            identifyTanorian(image);
            CheckForHoardes();

            if (currentTanorian.getName().equals(chainTarget)) {
                if (isHoard ) {
                    if (killHoards) {
                        attackSequence();
                        waitForColorAtPosition(new Point(500,500), Color.black); //Shouldn't matter, just needs to wait until entire screen is black
                        waitForColorNotAtPosition(new Point(500,500), Color.black); //Shouldn't matter, just needs to wait until entire screen is black
                        Thread.sleep(3000);
                    } else {
                        runSequence();
                        Thread.sleep(7000);
                    }
                } else {
                    attackSequence();
                    waitForColorAtPosition(new Point(500,500), Color.black); //Shouldn't matter, just needs to wait until entire screen is black
                    waitForColorNotAtPosition(new Point(500,500), Color.black); //Shouldn't matter, just needs to wait until entire screen is black
                    Thread.sleep(3000);
                }
            } else {
                runSequence();
                Thread.sleep(7000);
            }

        }
    }

    private static void attackSequence() throws InterruptedException {
        //Add proper wait for colors here instead of the sleeps (TODO)
        pressKey(KeyEvent.VK_BACK_SLASH, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_ENTER, (int)(Math.random() * ((150 - 100) + 1)));
        Thread.sleep(1000);
        pressKey(KeyEvent.VK_BACK_SLASH, (int)(Math.random() * ((150 - 100) + 1)));
        Thread.sleep(1000);
        pressKey(KeyEvent.VK_RIGHT, (int)(Math.random() * ((150 - 100) + 1)));
        Thread.sleep(1000);
        pressKey(KeyEvent.VK_ENTER, (int)(Math.random() * ((150 - 100) + 1)));
        Thread.sleep(1250);
        if (isHoard) {
            pressKey(KeyEvent.VK_BACK_SLASH, (int)(Math.random() * ((150 - 100) + 1)));
            Thread.sleep(1000);
            pressKey(KeyEvent.VK_LEFT, (int)(Math.random() * ((150 - 100) + 1)));
            Thread.sleep(50);
            pressKey(KeyEvent.VK_LEFT, (int)(Math.random() * ((150 - 100) + 1)));
            Thread.sleep(50);
            pressKey(KeyEvent.VK_DOWN, (int)(Math.random() * ((150 - 100) + 1)));
            Thread.sleep(1000);
            pressKey(KeyEvent.VK_ENTER, (int)(Math.random() * ((150 - 100) + 1)));
        }

    }
    private static void runSequence() {
        pressKey(KeyEvent.VK_BACK_SLASH, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_DOWN, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_DOWN, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_DOWN, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_ENTER, (int)(Math.random() * ((150 - 100) + 1)));
        pressKey(KeyEvent.VK_BACK_SLASH, (int)(Math.random() * ((150 - 100) + 1)));
    }
    private static void pressKey(int keycode, int delay) {
        r.keyPress(keycode);
        r.delay(delay);
        r.keyRelease(keycode);
    }
    private static void CheckForHoardes() throws InterruptedException {
        BufferedImage image;
        isHoard = false;
        if (r.getPixelColor(2275,339).equals(new Color(0,170,0))) { //The health bar of the SECOND tanorian in a hoard
            isHoard = true;
            System.out.println("Second tanorian found!");
           //Check second slot and so on
            totalEncounters++;
           image = r.createScreenCapture(new Rectangle(2167,272,191,42)); //The nametag of the SECOND tanorian in a hoard
           if (checkIfAnyPixelShiny(image)) {
               System.out.println("Shiny in hoard found");
               preventAFKKick();
           }
           if (r.getPixelColor(2231,492).equals(new Color(0,170,0))) { //The health bar of the THIRD tanorian in a hoard
               System.out.println("Third tanorian found!");
               totalEncounters++;
               image = r.createScreenCapture(new Rectangle(2167,422,191,42)); //The nametag of the THIRD tanorian in a hoard
               if (checkIfAnyPixelShiny(image)) {
                   System.out.println("Shiny in hoard found");
                   preventAFKKick();
               }
               if (r.getPixelColor(2257,637).equals(new Color(0,170,0))) { //The health bar of the FOURTH tanorian in a hoard
                   System.out.println("Fourth tanorian found!");
                   totalEncounters++;
                   image = r.createScreenCapture(new Rectangle(2167,582,191,42)); //The nametag of the FOURTH tanorian in a hoard
                   if (checkIfAnyPixelShiny(image)) {
                       System.out.println("Shiny in hoard found");
                       preventAFKKick();
                   }
                   if (r.getPixelColor(2231,785).equals(new Color(0,170,0))) {  //The health bar of the FIFTH tanorian in a hoard
                       System.out.println("Fifth tanorian found!");
                       totalEncounters++;
                       image = r.createScreenCapture(new Rectangle(2167,732,191,42)); //The nametag of the FIFTH tanorian in a hoard
                       if (checkIfAnyPixelShiny(image)) {
                           System.out.println("Shiny in hoard found");
                           preventAFKKick();
                       }
                   }
               }
           }
       }
    }

    private static void preventAFKKick() throws InterruptedException {
        pressKey(KeyEvent.VK_BACK_SLASH, 100);
        Random r = new Random();
        while (true) {
            File f = new File("stop.now");
            if (f.exists()) {
                break;
            }
            if (r.nextBoolean()) {
                pressKey(KeyEvent.VK_DOWN, (int)(Math.random() * ((350 - 250) + 1)));
            } else {
                pressKey(KeyEvent.VK_UP, (int)(Math.random() * ((350 - 250) + 1)));
            }
            Thread.sleep((int)(Math.random() * ((30000 - 10000) + 1)));
        }
    }

    private static boolean checkIfAnyPixelShiny(BufferedImage image) {
        ArrayList<Color> allColors = new ArrayList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3)), true));
            allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3) * 2), true));
        }
        for (Color color : allColors) {
            if (!color.equals(new Color(0,0,0))) {  //SHINY COLOR HERE AND MAKE IT TRUE INSTEAD OF ! (TODO)
                return false;
            }
        }
        return true;
    }
    private static void parseJson(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        Tanorian[] arr = gson.fromJson(new FileReader(path), Tanorian[].class);
        tanorians = new Tanorians(arr);
    }

    private static void identifyTanorian(BufferedImage image) throws IOException, InterruptedException {
        SaveToFile(image);
        currentTanorian = tanorians.find(image);
        totalEncounters++;
        if (currentTanorian == null) {
            System.out.println("New Tanorian Found! Enter Details:");
            preventAFKKick();
            Scanner sc = new Scanner(System.in);
            System.out.println("Shiny?");
            if (sc.nextBoolean()) {
                //Todo find shiny color
            }
            sc.nextLine();
            System.out.println("Name of tanorian");
            String name = sc.nextLine();
            System.out.println("Press anything when ready to proceed..");
            sc.nextLine();
            Thread.sleep(1000);
            currentTanorian = new Tanorian(getWhitePixels(image), name);
            tanorians.addTanorian(currentTanorian);
            sc.close();
        } else {
            System.out.println("Found a "+currentTanorian.getName()+" that has shiny status: "+currentTanorian.getShinyStatus()+". This is the "+totalEncounters+" encounter.");
            if ((currentTanorian.getShinyStatus() && allShinies)) {
                preventAFKKick();
            } else {
                //Todo
            }
        }

    }
        private static int getWhitePixels(BufferedImage image) {
            ArrayList<Color> allColors = new ArrayList<>();
            for (int x = 0; x < image.getWidth(); x++) {
                allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3)), true));
                allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3) * 2), true));
            }
            int c =0;
            for (Color color : allColors) {
                if (color.equals(Color.white)) {
                    c++;
                }
         }
        return c;
        }
    private static void SaveToFile(BufferedImage image) throws IOException {
        File outputfile = new File("text.jpg");
        ImageIO.write(image, "jpg", outputfile);
    }


    private static void betterStrafe(int maxTimeInMillis) {
        Color color;
        long start = System.currentTimeMillis() - totalOffset;
        r.keyPress(keyCode);
        do {
            long current = System.currentTimeMillis();
            long elapsed = current - start;
            totalOffset = elapsed;
            if (elapsed >= maxTimeInMillis) {
                totalOffset = 0;
                if (keyCode == KeyEvent.VK_D) {
                    keyCode = KeyEvent.VK_A;
                } else {
                    keyCode = KeyEvent.VK_D;
                }
                start = current;

                if (keyCode == KeyEvent.VK_D) {
                    r.keyRelease(KeyEvent.VK_A);
                    r.keyPress(KeyEvent.VK_D);
                } else {
                    r.keyRelease(KeyEvent.VK_D);
                    r.keyPress(KeyEvent.VK_A);
                }
            }
            color = r.getPixelColor(150,1312);
        } while (!color.equals(Color.black));
        r.keyRelease(keyCode);

    }



    private static void waitForColorAtPosition(Point position, Color color) {
        Color currentColors;
        do {
            currentColors = r.getPixelColor(position.x, position.y);
        } while(!currentColors.equals(color));
    }
    private static void waitForColorNotAtPosition(Point position, Color color) {
        Color currentColors;
        do {
            currentColors = r.getPixelColor(position.x, position.y);
        } while(currentColors.equals(color));
    }
}





