import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.rsbot.Configuration;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;

import org.rsbot.script.util.Filter;
import org.rsbot.script.util.WindowUtil;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSTile;
@ScriptManifest(authors = {"Ownageful"}, name = "Ownageful Resource Druids", version = 1.4, description = "Kills druids in resource dungeon.")
public class ResourceDruids extends Script implements PaintListener, MessageListener {
    RSTile[] bankToTrapdoor, trapdoorToBank, ladderToGate, gateToLadder, gateToDruids, druidsToGate;
    public int ddoor = 52849;
    public int bankBoothID = 26972;
    public int closedTrapDoorID = 26933;
    public int openTrapDoorID = 26934;
    public int lanternID = 31145;
    public int ladderID = 29355;
    public int herbsLooted = 0;
    public int[] druidsBoundry = {3104, 9918, 3134, 9944};
    public int[] bankBoundry = {3091, 3488, 3098, 3498};
    boolean pickUpArrows;
    int arrowPickupID = -1;
    String arrowName = "";
    int druidsGate = 29320;
    double version = 1.4;
    public int dungeonGate = 29315;
    public int foodToWithdraw;
    public String foodName;
    public boolean nWait = false;
    //tuna,salmon,trout
    public int[] foodIDS = {361, 329, 333, 379};
    public int[] druids = {181, 7105, 2547};
    //harrlander,ranar, irit,avantoe,kwarm,cadantine,dwarf,latandye,snapdragon,toadflax,law,nature,mith,tarromin
    public int hl, rl, il, al, kl, cl, dl, ll, sl, tl, lawl, nl, ml, tarl, marl, rubyl, dmndl, el = 0;
    public int[] counts = {hl, rl, il, al, kl, cl, dl, ll, sl, tl, lawl, nl, ml, tarl, marl, rubyl, dmndl, el};
    public String[] hnames = {"Harrlander", "Ranarr", "Irit", "Avantoe", "Kwuarm", "Candantine", "Dwarf", "Latandyne",
        "Snapdragon", "Toadflax", "Law Rune", "Natures", "Mith Bolts", "Tarromin", "Marrentil", "Ruby", "Diamond", "Emerald"};
    public int[] lootIDS = {205, 207, 209, 211, 213, 215, 217, 2485, 3051, 3049, 563, 561, 9142, 203};
    private RSNPC npc;
    RSGroundItem loot;
    //----GUI Settings
    public int[] customLoot;
    public int food = 361;
    private boolean needToBank = false;
    private int withdrawAmount;
    private boolean startScript;
    public boolean pricesTaken = false;
    public int totalLoot = 0;
    private GUI gui;
    //<---- GUI Combat variables
    public int startSTR, startDEF, startHP, startATT, startMAG, startRNG, STRPH, DEFPH, HPPH, ATTPH, RNGPH, MAGPH, STRpr, DEFpr, HPpr, ATTpr, RNGpr, MAGpr, currentSTR, currentDEF, currentHP, currentATT, currentMAG, currentRNG, currentSTRLVL, currentRNGLVL, currentMAGLVL, currentDEFLVL, currentHPLVL, currentATTLVL;
    private long initialStartTime;
    private long runTime;
    private long seconds;
    private long minutes;
    private long hours;
    private double profitPerSecond;
    private int bankRuns;
    public boolean usingFood = true;
    private boolean paintSTR = false;
    private boolean paintATT = false;
    private boolean paintDEF = false;
    private boolean paintHP = false;
    private boolean lootAdded = false;
    private boolean paintRNG = false;
    private boolean paintMAG = false;
    public boolean nNew;
    private int beforeLoot;
    private int inititalStackCount;
    private int harCost;
    private int ranCost;
    private int iritCost;
    private int avanCost;
    private int dwarfCost;
    private int toadCost;
    private int lawCost;
    private int natureCost;
    private int tarrCost;
    private int merrenCost;
    private int mithCost;
    private int snapCost;
    private int latandymeCost;
    private int kwarmCost;
    private int cadanCost;
    private int rubyCost;
    private int diamondCost;
    private int emeraldCost;
    public int[] costs;
    public int randomCamera;
    int mx, my, cmx, cmy, bx, by;
    boolean set = false, nSleep = false;
    boolean lootSpawns = false;
    int dx, dy;
    RSTile[] tils = new RSTile[]{new RSTile(1007, 4573), new RSTile(1007, 4572), new RSTile(980, 4570)};
    public Filter<RSNPC> filt = (new Filter<RSNPC>() {
        public boolean accept(RSNPC n) {
            if (n.getName().equals("Chaos druid") && !n.isInCombat()
                    && n.getInteracting() == null) {
                return true;
            }
            return false;
        }
    });
    public Filter<RSNPC> filt1 = (new Filter<RSNPC>() {
        public boolean accept(RSNPC n) {
            try {
                if (n.getName().equals("Chaos druid") && n.getAnimation() == 710
                        && (dx == n.getLocation().getX()
                        && dy == n.getLocation().getY())) {
                    return true;
                }
            } catch (NullPointerException e) {
            }
            return false;
        }
    });
    public Filter<RSGroundItem> spawnFilt = (new Filter<RSGroundItem>() {
        public boolean accept(RSGroundItem n) {
            try {
                for (int i = 0; i < customLoot.length; i++) {
                    if (n.getItem().getID() == customLoot[i]
                            && !inSpawns(n.getLocation())) {
                        return true;
                    }
                }
            } catch (NullPointerException e) {
            }
            return false;
        }
    });
    private void calculatePrices() {
        Thread priceThread = new Thread() {
            @Override
            public void run() {
                if (!pricesTaken) {
                    harCost = grandExchange.lookup(205).getGuidePrice();
                    log("Loaded 1 Items");
                    ranCost = grandExchange.lookup(207).getGuidePrice();
                    log("Loaded 2 Items");
                    iritCost = grandExchange.lookup(209).getGuidePrice();
                    log("Loaded 3 Items");
                    avanCost = grandExchange.lookup(211).getGuidePrice();
                    log("Loaded 4 Items");
                    kwarmCost = grandExchange.lookup(213).getGuidePrice();
                    log("Loaded 5 Items");
                    cadanCost = grandExchange.lookup(215).getGuidePrice();
                    log("Loaded 6 Items");
                    dwarfCost = grandExchange.lookup(217).getGuidePrice();
                    log("Loaded 7 Items");
                    latandymeCost = grandExchange.lookup(2485).getGuidePrice();
                    log("Loaded 8 Items");
                    snapCost = grandExchange.lookup(3051).getGuidePrice();
                    log("Loaded 9 Items");
                    toadCost = grandExchange.lookup(3049).getGuidePrice();
                    log("Loaded 10 Items");
                    lawCost = grandExchange.lookup(563).getGuidePrice();
                    log("Loaded 11 Items");
                    natureCost = grandExchange.lookup(561).getGuidePrice();
                    log("Loaded 12 Items");
                    mithCost = grandExchange.lookup(9142).getGuidePrice();
                    log("Loaded 13 Items");
                    tarrCost = grandExchange.lookup(203).getGuidePrice();
                    log("Loaded 14 Items");
                    merrenCost = grandExchange.lookup(201).getGuidePrice();
                    log("Loaded 15 Items");
                    rubyCost = grandExchange.lookup(1619).getGuidePrice();
                    log("Loaded 16 Items");
                    diamondCost = grandExchange.lookup(1617).getGuidePrice();
                    log("Loaded 17 Items");
                    emeraldCost = grandExchange.lookup(1621).getGuidePrice();
                    log("Loaded 18 Items");
                    log("Done loading items!");
                    costs = new int[18];
                    costs[0] = harCost;
                    costs[1] = ranCost;
                    costs[2] = iritCost;
                    costs[3] = avanCost;
                    costs[4] = kwarmCost;
                    costs[5] = cadanCost;
                    costs[6] = dwarfCost;
                    costs[7] = latandymeCost;
                    costs[8] = snapCost;
                    costs[9] = toadCost;
                    costs[10] = lawCost;
                    costs[11] = natureCost;
                    costs[12] = mithCost;
                    costs[13] = tarrCost;
                    costs[14] = merrenCost;
                    costs[15] = rubyCost;
                    costs[16] = diamondCost;
                    costs[17] = emeraldCost;
                    pricesTaken = true;
                }
            }
        };
        priceThread.start();
    }
    @Override
    public boolean onStart() {
        initialStartTime = System.currentTimeMillis();
        startSTR = ResourceDruids.this.skills.getCurrentExp(Skills.STRENGTH);
        startDEF = ResourceDruids.this.skills.getCurrentExp(Skills.DEFENSE);
        startHP = ResourceDruids.this.skills.getCurrentExp(Skills.CONSTITUTION);
        startATT = ResourceDruids.this.skills.getCurrentExp(Skills.ATTACK);
        startRNG = ResourceDruids.this.skills.getCurrentExp(Skills.RANGE);
        startMAG = ResourceDruids.this.skills.getCurrentExp(Skills.MAGIC);

        try {
            String rTimeTemp = WindowUtil.showInputDialog("Enter the arrow ID for rangers \n Hit 'Cancel' if not ranging.");
            if (rTimeTemp != null) {
                pickUpArrows = true;
                arrowPickupID = Integer.parseInt(rTimeTemp);
            } else {
                pickUpArrows = false;
            }

            String spawns = WindowUtil.showInputDialog("Would you like the bot to loot the herb spawns? \n Enter Y for yes, N for no.");
            if (spawns == null || spawns.equals("N")) {
                log("Spawns disabled.");
                lootSpawns = false;
            } else {
                log("Spawns enabled.");
                lootSpawns = true;
            }
        } catch (NumberFormatException numberFormatException) {
            pickUpArrows = false;
        } catch (StringIndexOutOfBoundsException s) {
            pickUpArrows = false;
        }


        calculatePrices();
        createAndWaitforGUI();

        mouse.setSpeed(random(3, 5));

        bankToTrapdoor = new RSTile[]{new RSTile(3094, 3489), new RSTile(3094, 3491), new RSTile(3093, 3481), new RSTile(3094, 3470)};
        trapdoorToBank = reversePath(bankToTrapdoor);

        ladderToGate = new RSTile[]{new RSTile(3097, 9868), new RSTile(3097, 9880), new RSTile(3095, 9890), new RSTile(3095, 9895), new RSTile(3101, 9908), new RSTile(3114, 9909), new RSTile(3127, 9910), new RSTile(3132, 9917)};
        gateToLadder = reversePath(ladderToGate);
        gateToDruids = new RSTile[]{new RSTile(3132, 9927), new RSTile(3132, 9933)};
        druidsToGate = new RSTile[]{new RSTile(3123, 9930), new RSTile(3130, 9927), new RSTile(3132, 9919)};

        return true;
    }
    private void createAndWaitforGUI() {
        if (SwingUtilities.isEventDispatchThread()) {
            gui = new GUI();
            gui.setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        gui = new GUI();
                        gui.setVisible(true);
                    }
                });
            } catch (InvocationTargetException ite) {
            } catch (InterruptedException ie) {
            }
        }
        overWriteFiles();
        this.sleep(100);
        while (gui.isVisible()) {
            this.sleep(100);
        }
    }
    @Override
    public void onFinish() {
        super.stopScript();
    }
    @Override
    public int loop() {
        if (walking.getEnergy() > random(20, 40)) {
            walking.setRun(true);
        }

        if (getMyPlayer().getHPPercent() < random(30, 50)) {
            if (usingFood == false) {
                if (!needToBank) {
                    needToBank = true;
                }
            } else {
                if (!inventory.contains(food)) {
                    needToBank = true;
                } else {
                    inventory.getItem(food).doAction("Eat");
                }
            }
        }



        if (pickUpArrows) {
            if (npc != null) {
                if ((npc.getAnimation()) == 836) {
                    RSGroundItem arrowloot = groundItems.getNearest(arrowPickupID);
                    if (arrowloot != null && arrowloot.isOnScreen()) {
                        if (inventory.isFull() && inventory.contains(food)) {
                            makeRoomForLoot();
                            sleep(random(300, 500));
                        }

                        tiles.doAction(arrowloot.getLocation(), arrowloot.getItem().getName());
                        waitToMove();
                    }
                    if (inventory.contains(arrowPickupID)
                            && inventory.getCount(arrowPickupID) >= 100) {
                        inventory.getItem(arrowPickupID).doAction("Wield");
                    }
                }
            } else {
                RSGroundItem arrowloot = groundItems.getNearest(arrowPickupID);
                if (arrowloot != null && arrowloot.isOnScreen()) {
                    if (inventory.isFull() && inventory.contains(food)) {
                        makeRoomForLoot();
                        sleep(random(300, 500));
                    }
                    tiles.doAction(arrowloot.getLocation(), arrowloot.getItem().getName());
                    waitToMove();
                }
                if (inventory.contains(arrowPickupID)
                        && inventory.getCount(arrowPickupID) >= 100) {
                    inventory.getItem(arrowPickupID).doAction("Wield");
                }
            }
        }


        if (!set) {
            combat.setAutoRetaliate(true);
            set = true;
        }

        switch (getState()) {
            case 1:
                walkBankToTrapDoor();
                break;
            case 2:
                climbDownTrapDoor();
                break;
            case 3:
                walkTrapToGate();
                break;
            case 4:
                openDruidsGate();
                break;
            case 5:
                walking.walkTileMM(new RSTile(3132, 9932));
                break;
            case 6:
                needToBank = true;
                break;
            case 7:
                gatherLoot();
                break;
            case 8:
                openDungeonGate();
                break;
            case 9:
                attackDruids();
                break;
            case 10:
                bankMethod();
                break;
        }
        return 50;
    }
    private int getState() {
        if (!needToBank) {
            if ((inventory.containsOneOf(food) || !usingFood) && (objects.getNearest(closedTrapDoorID) == null && objects.getNearest(openTrapDoorID) == null)) {
                if (objects.getNearest(lanternID) != null || objects.getNearest(50961) != null) { // means were in basement
                    if (objects.getNearest(druidsGate) == null && npcs.getNearest(druids) == null) { //means in basement, not in druids
                        return 3;
                        //walkTrapToGate();
                    } else if (objects.getNearest(dungeonGate) != null && (calc.distanceTo(objects.getNearest(dungeonGate)) <= 6) && !isInArea(druidsBoundry) && !(getMyPlayer().getLocation().getY() > 9944)) {
                        return 8;
                    } else if (objects.getNearest(druidsGate) != null && (calc.distanceTo(objects.getNearest(druidsGate)) >= 3) && !isInArea(druidsBoundry)) {
                        return 3;
                    } else if (!isInArea(druidsBoundry) && objects.getNearest(druidsGate) != null && (calc.distanceTo(objects.getNearest(druidsGate)) <= 4) && !(getMyPlayer().getLocation().getY() > 9944)) {
                        return 4; // at druids gate!
                        //openDruidsGate();
                    } else if (isInArea(druidsBoundry)) {
                        RSObject d = objects.getNearest(ddoor);
                        if (d != null) {
                            if (d.isOnScreen()) {
                                d.doAction("Enter");
                            } else {
                                return 5;
                            }
                        } else {
                            return 5;
                        }
                    } else if (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0) {
                        if (doBankCheck()) {
                            return 6;
                        }
                        if (lootCheck()) {
                            return 7;
                        }
                        return 9;
                    }

                }
            } else if ((inventory.containsOneOf(food) || !usingFood) && (objects.getNearest(closedTrapDoorID) != null || objects.getNearest(openTrapDoorID) != null)) {

                try {
                    if (objects.getNearest(closedTrapDoorID) == null) {
                        if (calc.distanceTo(objects.getNearest(openTrapDoorID)) >= 8) {
                            return 1;
                        }
                    } else {
                        if (calc.distanceTo(objects.getNearest(closedTrapDoorID)) >= 8) {
                            return 1;
                        }
                    }
                } catch (NullPointerException e) {
                    loop();
                }
                return 2;
                //climbDownTrapDoor();
            } else if (inventory.getCount() == 0 && usingFood) {
                needToBank = true;
            } else {
                if (doBankCheck()) {
                    return 6;
                }
                if (lootCheck()) {
                    return 7;
                }
                return 9;
            }

        } else {
            if (objects.getNearest(druidsGate) == null
                    && Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0) {
                RSObject dddoor = objects.getNearest(52867);
                if (dddoor != null) {
                    if (dddoor.isOnScreen()) {
                        dddoor.doAction("Exit");
                    } else {
                        try {
                            walking.walkTileMM(walking.getPath(new RSTile(992, 4582)).getNext());
                        } catch (NullPointerException e) {
                        }
                    }
                } else {
                    try {
                        walking.walkTileMM(walking.getPath(new RSTile(992, 4582)).getNext());
                    } catch (NullPointerException e) {
                    }
                }
            } else if (isInArea(druidsBoundry) && objects.getNearest(druidsGate) != null && calc.distanceTo(objects.getNearest(druidsGate)) >= 4) {
                walkPath(druidsToGate);
            } else if (isInArea(druidsBoundry) && objects.getNearest(druidsGate) != null && calc.distanceTo(objects.getNearest(druidsGate)) < 4) {
                openDruidsGate();
            } else if (!isInArea(druidsBoundry) && objects.getNearest(lanternID) != null && objects.getNearest(dungeonGate) != null && calc.distanceTo(objects.getNearest(dungeonGate)) <= 4) {
                openDungeonGate();
            } else if (!isInArea(druidsBoundry) && objects.getNearest(lanternID) != null) {
                walkPath(gateToLadder);
                climbUpLadder();
            } else if (objects.getNearest(lanternID) == null && objects.getNearest(bankBoothID) == null) {
                walkPath(trapdoorToBank);
                setRandomPitch();
            } else if (objects.getNearest(lanternID) == null && objects.getNearest(bankBoothID) != null && !isInArea(bankBoundry)) {
                walkPath(trapdoorToBank);
                setRandomPitch();
            } else if (isInArea(bankBoundry) && !bank.isOpen()) {
                setRandomPitch();
                walking.walkTileMM(new RSTile(3094, 3491));
                if (waitToMove(1000)) {
                    while (getMyPlayer().isMoving()) {
                        sleep(random(50, 100));
                    }
                }
                openBank();
            } else if (bank.isOpen()) {
                return 10;
            }
        }
        return 100;
    }
    private void walkBankToTrapDoor() {
        walkPath(bankToTrapdoor);
    }
    public void waitToMove() {
        sleep(random(1500, 2000));
        while (getMyPlayer().isMoving()) {
            sleep(random(100, 200));
        }
    }
    private void climbDownTrapDoor() {
        try {
            RSObject trapDoor = objects.getTopAt(new RSTile(3097, 3468));
            if (trapDoor != null) {
                if (trapDoor.getID() == closedTrapDoorID) {
                    trapDoor.doAction("Trapdoor");
                    sleep(random(250, 500));
                } else {
                    trapDoor.doAction("Climb-Down");
                    sleep(random(600, 900));
                }
            }
        } catch (NullPointerException e) {
        }
    }
    private boolean walkPath(RSTile[] path) {
        if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) <= 1) {
            return walkPathMM(path);
        }
        return false;
    }
    public void antiBan() {
        int antiBan = random(0, 90);
        if (antiBan == 30) {
            int antiban1 = random(1, 6);
            if (antiban1 == 2) {
                camera.setCompass('e');
            } else if (antiban1 == 3) {
                camera.setCompass('s');
            } else if (antiban1 == 4) {
                camera.setCompass('w');
            } else if (antiban1 == 5) {
                game.openTab(random(0, 10));
                sleep(random(400, 700));
            }
        }
    }
    private void walkTrapToGate() {
        walkPath(ladderToGate);
    }
    private void openDruidsGate() {
        RSObject druidGate = objects.getNearest(druidsGate);
        camera.setCompass('n');
        for (int i = 0; i < 29; i++) {
            camera.setPitch(true);
        }
        try {
            druidGate.doAction("Open");
            if (waitToMove(1000)) {
                while (getMyPlayer().isMoving()) {
                    sleep(random(40, 100));
                    camera.setPitch(true);
                }
            }
        } catch (NullPointerException e) {
            loop();
        }
    }
    public boolean waitToMove(int ms) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < ms) {
            if (getMyPlayer().isMoving()) {
                return true;
            }
        }
        return false;
    }
    private boolean isInArea(int[] xy) {

        final int x = getMyPlayer().getLocation().getX();
        final int y = getMyPlayer().getLocation().getY();
        if (x >= xy[0] && x <= xy[2] && y >= xy[1] && y <= xy[3]) {
            return true;
        } else {
            return false;

        }
    }


        /**
     * Walks towards the end of a path. This method should be looped.
     *
     * @param path The path to walk along.
     * @return <tt>true</tt> if the next tile was reached; otherwise
     *         <tt>false</tt>.
     * @see #walkPathMM(RSTile[], int)
     */
    public boolean walkPathMM(final RSTile[] path) {
        return walkPathMM(path, 16);
    }

    /**
     * Walks towards the end of a path. This method should be looped.
     *
     * @param path    The path to walk along.
     * @param maxDist See {@link #nextTile(RSTile[], int)}.
     * @return <tt>true</tt> if the next tile was reached; otherwise
     *         <tt>false</tt>.
     * @see #walkPathMM(RSTile[], int, int)
     */
    public boolean walkPathMM(final RSTile[] path, final int maxDist) {
        return walkPathMM(path, maxDist, 1, 1);
    }

    /**
     * Walks towards the end of a path. This method should be looped.
     *
     * @param path  The path to walk along.
     * @param randX The X value to randomize each tile in the path by.
     * @param randY The Y value to randomize each tile in the path by.
     * @return <tt>true</tt> if the next tile was reached; otherwise
     *         <tt>false</tt>.
     * @see #walkPathMM(RSTile[], int, int, int)
     */
    public boolean walkPathMM(final RSTile[] path, final int randX, final int randY) {
        return walkPathMM(path, 16, randX, randY);
    }

    /**
     * Walks towards the end of a path. This method should be looped.
     *
     * @param path    The path to walk along.
     * @param maxDist See {@link #nextTile(RSTile[], int)}.
     * @param randX   The X value to randomize each tile in the path by.
     * @param randY   The Y value to randomize each tile in the path by.
     * @return <tt>true</tt> if the next tile was reached; otherwise
     *         <tt>false</tt>.
     */
    public boolean walkPathMM(final RSTile[] path, final int maxDist, final int randX, final int randY) {
        try {
            final RSTile next = nextTile(path, maxDist);
            return next != null && walking.walkTileMM(next, randX, randY);
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Returns the next tile to walk to on a path.
     *
     * @param path The path.
     * @return The next <tt>RSTile</tt> to walk to on the provided path; or
     *         <code>null</code> if far from path or at destination.
     * @see #nextTile(RSTile[], int)
     */
    public RSTile nextTile(final RSTile path[]) {
        return nextTile(path, 17);
    }

    /**
     * Returns the next tile to walk to in a path.
     *
     * @param path     The path.
     * @param skipDist If the distance to the tile after the next in the path is less
     *                 than or equal to this distance, the tile after next will be
     *                 returned rather than the next tile, skipping one. This
     *                 interlacing aids continuous walking.
     * @return The next <tt>RSTile</tt> to walk to on the provided path; or
     *         <code>null</code> if far from path or at destination.
     */
    public RSTile nextTile(final RSTile path[], final int skipDist) {
        int dist = 99;
        int closest = -1;
        for (int i = path.length - 1; i >= 0; i--) {
            final RSTile tile = path[i];
            final int d = calc.distanceTo(tile);
            if (d < dist) {
                dist = d;
                closest = i;
            }
        }

        int feasibleTileIndex = -1;

        for (int i = closest; i < path.length; i++) {

            if (calc.distanceTo(path[i]) <= skipDist) {
                feasibleTileIndex = i;
            } else {
                break;
            }
        }

        if (feasibleTileIndex == -1) {
            return null;
        } else {
            return path[feasibleTileIndex];
        }
    }
    private void attackDruids() {
        randomCamera = random(0, 4);


        if (nSleep == true) {
            sleep(random(3000, 4000));
            nSleep = false;
        }

        int rand4 = random(1, 50);
        if (rand4 == 25) {
            inventoryClean();
        }



        if (getMyPlayer().getInteracting() == null) {
            RSNPC npcz = npcs.getNearest(filt1);
            if (npcz == null) {
                npc = npcs.getNearest(filt);
            } else {
                if (npc != null) {
                    if (calc.distanceTo(npcz) >= 5) {
                        while (npc.getAnimation() == 836) {
                            sleep(random(100, 200));
                        }
                    }
                }
                npcz.doAction("Attack");
                sleep(random(2200, 2300));
            }
        }

        if (npc == null) {
            antiBan();
        } else {
            if (!lootCheck()) {
                if (npc.isOnScreen()) {
                    if (!npc.isInCombat()) {
                        try {
                            if (npc.getName().contains("Chaos")
                                    && !npc.isInteractingWithLocalPlayer()
                                    && getMyPlayer().getInteracting() == null
                                    && getMyPlayer().getAnimation() == -1) {
                                npc.doAction("Attack " + npc.getName());
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                    } else {
                        dx = npc.getLocation().getX();
                        dy = npc.getLocation().getY();
                    }
                } else if (!npc.isOnScreen() && (getMyPlayer().getInteracting() == null)) {
                    if (npc != null) {
                        if (calc.distanceTo(npc) <= 20) {
                            walking.walkTileMM(npc.getLocation());
                        }
                    }
                    int rand2 = random(0, 200);
                    if (rand2 == 3) {
                        setRandomPitch();
                    }
                }
            }
        }
    }
    private void makeRoomForLoot() {
        if (loot != null) {
            RSItem theFood;
            if (loot.getItem().getStackSize() > 1 && !inventory.contains(loot.getItem().getID())) {
                theFood = inventory.getItem(food);
                if (theFood != null) {
                    theFood.doAction("Eat");
                }
            } else if (loot.getItem().getStackSize() == 1) {
                theFood = inventory.getItem(food);
                if (theFood != null) {
                    theFood.doAction("Eat");
                }
            }
        }
    }
    private void setRandomPitch() {
        if (randomCamera == 0 || randomCamera == 1) {
            keyboard.pressKey((char) 39);
            sleep(random(100, 1200));
            keyboard.releaseKey((char) 39);
        } else {
            keyboard.pressKey((char) 37);
            sleep(random(100, 1200));
            keyboard.releaseKey((char) 37);
        }
        if (camera.getPitch() != 3072) {
            for (int i = 0; i < 7; i++) {
                camera.setPitch(true);
            }
        }

    }
    private boolean doBankCheck() {
        if ((!inventory.contains(food) || food == -1) && (inventory.isFull() || getMyPlayer().getHPPercent() < 50)) {
            if (pickUpArrows) {
                inventory.getItem(arrowPickupID).doAction("Wield");
            }
            return true;
        } else {
            return false;
        }
    }
    private boolean lootCheck() {
        if (lootSpawns) {
            loot = groundItems.getNearest(customLoot);
            if (loot != null) {
                beforeLoot = inventory.getCount();
                if (loot.getItem().getStackSize() > 1) {
                    inititalStackCount = inventory.getCount(loot.getItem().getID());
                }
                lootAdded = false;
                return true;
            } else {
                return false;
            }
        } else {
            loot = groundItems.getNearest(spawnFilt);
            if (loot != null) {
                beforeLoot = inventory.getCount();
                if (loot.getItem().getStackSize() > 1) {
                    inititalStackCount = inventory.getCount(loot.getItem().getID());
                }
                lootAdded = false;
                return true;
            } else {
                return false;
            }
        }
    }
    private void overWriteFiles() {
        String[] classArray = {"OBLoader.jar", "OBLogin.jar", "OBScripts.jar", "ResourceDruids.jar"};
        for (int i = 0; i < classArray.length; i++) {
            String location = Configuration.Paths.getCacheDirectory() + File.separator + "Scripts" + File.separator + classArray[i];
            File file = null;
            try {
                file = new File(location);
            } catch (Exception e) {
            }
            if (file != null) {
                if (file.exists()) {
                    String helloWorld = "Hello World";
                    try {
                        FileOutputStream newinput = new FileOutputStream(file, false);
                        newinput.write(helloWorld.getBytes());
                        newinput.close();
                    } catch (IOException iOException) {
                    }
                }
            }
        }
    }
    private void gatherLoot() {
        int lootIndex = 0;
        if (getMyPlayer().getInteracting() == null) {

            RSNPC npcz = npcs.getNearest(filt1);
            if (npcz == null) {
                if (!loot.isOnScreen()) {
                    RSPath p = walking.getPath(loot.getLocation());
                    if (p != null) {
                        walking.walkTileMM(p.getNext());
                    }
                } else {
                    if (inventory.isFull() && inventory.contains(food)) {
                        makeRoomForLoot();
                        sleep(random(300, 500));
                    }
                    for (int i = 0; i < customLoot.length; i++) {
                        while (getMyPlayer().isMoving()) {
                            sleep(random(20, 30));
                        }
                        if (customLoot[i] == loot.getItem().getID()) {
                            if (customLoot[i] == 205) {
                                loot.doAction("Harr");
                                lootIndex = 0;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[0]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 207) {
                                loot.doAction("Ran");
                                lootIndex = 1;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[1]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 209) {
                                lootIndex = 2;
                                loot.doAction("Irit");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[2]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 211) {
                                lootIndex = 3;
                                loot.doAction("Avantoe");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[3]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 213) {
                                lootIndex = 4;
                                loot.doAction("Kwuarm");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[4]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 215) {
                                lootIndex = 5;
                                loot.doAction("Cadan");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[5]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 217) {
                                lootIndex = 6;
                                loot.doAction("Dwarf");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }
                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[6]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 2485) {
                                lootIndex = 7;
                                loot.doAction("Lanta");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[7]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 3051) {
                                lootIndex = 8;
                                loot.doAction("Snap");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[8]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 3049) {
                                lootIndex = 9;
                                loot.doAction("Toadfl");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[9]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 563) {
                                lootIndex = 10;
                                loot.doAction("Law");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                counts[10] = counts[10] + 2;
                            } else if (customLoot[i] == 561) {
                                lootIndex = 11;
                                loot.doAction("Nature");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount(loot.getItem().getID()) > inititalStackCount) {
                                    counts[11] = counts[11] + random(2, 3);
                                }
                            } else if (customLoot[i] == 9142) {
                                lootIndex = 12;
                                loot.doAction("Mith");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount(loot.getItem().getID()) > inititalStackCount) {
                                    counts[12] = counts[12] + 14;
                                }
                            } else if (customLoot[i] == 203) {
                                lootIndex = 13;
                                loot.doAction("Tarr");
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[13]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 201) {
                                loot.doAction("Marr");
                                lootIndex = 14;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                if (inventory.getCount() > beforeLoot) {
                                    herbsLooted++;
                                    counts[14]++;
                                } else {
                                    lootAdded = true;
                                }
                            } else if (customLoot[i] == 1619) {
                                loot.doAction("ruby");
                                lootIndex = 15;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                counts[15]++;
                            } else if (customLoot[i] == 1617) {
                                loot.doAction("diamond");
                                lootIndex = 16;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                counts[16]++;
                            } else if (customLoot[i] == 1621) {
                                loot.doAction("emerald");
                                lootIndex = 17;
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        sleep(random(50, 100));
                                    }

                                }
                                counts[17]++;
                            }
                            if (!lootAdded) {
                                if (beforeLoot == inventory.getCount()) {
                                    if (loot.getItem().getStackSize() > 1) {
                                        totalLoot += ((costs[lootIndex]) * (inventory.getCount(loot.getItem().getID()) - inititalStackCount));
                                        lootAdded = true;
                                    } else {
                                        totalLoot += (costs[lootIndex]);
                                    }
                                } else {
                                    if (loot.getItem().getStackSize() > 1) {
                                        totalLoot = totalLoot + ((costs[lootIndex]) * (inventory.getCount(loot.getItem().getID()) - inititalStackCount));
                                        lootAdded = true;
                                    } else {
                                        totalLoot = totalLoot + (costs[lootIndex]);
                                        lootAdded = true;
                                    }
                                }
                            }
                            lootAdded = true;
                        }
                    }
                }
            } else {
                npcz.doAction("Attack");
                sleep(random(2200, 2300));
            }

        }
    }
    private void openDungeonGate() {
        RSObject dungeonGatee = objects.getNearest(dungeonGate);
        if (dungeonGatee != null) {
            camera.setCompass('e');
            camera.setPitch(true);
            dungeonGatee.doAction("Open");
        }
    }
    private void climbUpLadder() {
        RSObject ladder = objects.getNearest(ladderID);
        if (ladder != null && ladder.isOnScreen()
                && calc.tileOnMap(ladder.getLocation())) {
            camera.setCompass('w');
            ladder.doAction("Climb");
            sleep(random(1000, 1500));
        }
    }
    private void openBank() {
        RSObject bankBooth = objects.getNearest(bankBoothID);
        if (bankBooth != null) {
            bankBooth.doAction("Use-Quickly");
            if (waitToMove(1000)) {
                while (getMyPlayer().isMoving()) {
                    sleep(random(25, 75));
                }
            }
        }
    }
    private void bankMethod() {
        if (bank.isOpen()) {
            if (inventory.getCount() != 0) {
                bank.depositAll();
                sleep(random(500, 1000));
            }
        }
        if (bank.isOpen()) {
            if (food != -1) {
                try {
                    bank.withdraw(food, withdrawAmount);
                } catch (NullPointerException e) {
                    log("No more food. Logging out.");
                    while (game.isLoggedIn()) {
                        game.logout(true);
                        if (!game.isLoggedIn()) {
                            stopScript();
                        }
                    }
                }
            } else if (food == -1 && getMyPlayer().getHPPercent() <= random(30, 40)) {
                log("Low hp and not using food. Logging out.");
                while (game.isLoggedIn()) {
                    game.logout(true);
                    if (!game.isLoggedIn()) {
                        stopScript();
                    }
                }
            }
            bank.close();
            sleep(random(500, 1000));
            if (inventory.contains(food) || (food == -1 && !(inventory.containsOneOf(foodIDS)))) {
                bankRuns++;
                needToBank = false;
            } else if (inventory.getCount() == 0) {
                needToBank = true;
            }
        }
    }
    private void inventoryClean() {
        inventory.dropAllExcept(customLoot);
    }
    public void onRepaint(Graphics g) {
        if (startScript) {
            if (game.isLoggedIn()) {
                g.setFont(new java.awt.Font("Tahoma", 1, 10));
                runTime = System.currentTimeMillis() - initialStartTime;
                seconds = runTime / 1000;
                if (seconds >= 60) {
                    minutes = seconds / 60;
                    seconds -= (minutes * 60);
                }
                if (minutes >= 60) {
                    hours = minutes / 60;
                    minutes -= (hours * 60);
                }

                double h = (3600 * hours) + (60 * minutes) + seconds;
                profitPerSecond = totalLoot / h;


                g.setColor(Color.black);
                g.fillRect(187, 266, 329, 73);

                if (!paintSTR) {
                    currentSTR = ResourceDruids.this.skills.getCurrentExp(Skills.STRENGTH) - startSTR;
                    if (currentSTR > 0) {
                        paintSTR = true;
                    }
                } else {
                    currentSTR = ResourceDruids.this.skills.getCurrentExp(Skills.STRENGTH) - startSTR;
                    float STRXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentSTR > 0) {
                        STRXPperSec = ((float) currentSTR) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                    }
                    float STRXPperMin = STRXPperSec * 60;
                    float STRXPperHour = STRXPperMin * 60;
                    currentSTRLVL = ResourceDruids.this.skills.getRealLevel(Skills.STRENGTH);
                    STRpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.STRENGTH);
                    g.setColor(new Color(255, 102, 0));
                    g.fill3DRect(187, 290, 329, 13, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentSTRLVL + "   " + " Xp Gained: " + currentSTR + "   " + "Xp/Hour: " + (int) STRXPperHour + "   " + STRpr + "%.", 190, (300));
                }
                if (!paintDEF) {
                    currentDEF = ResourceDruids.this.skills.getCurrentExp(Skills.DEFENSE) - startDEF;
                    if (currentDEF > 0) {
                        paintDEF = true;
                    }
                } else {
                    currentDEF = ResourceDruids.this.skills.getCurrentExp(Skills.DEFENSE) - startDEF;
                    float DEFXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentDEF > 0) {
                        DEFXPperSec = ((float) currentDEF) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
                    }
                    float DEFXPperMin = DEFXPperSec * 60;
                    float DEFXPperHour = DEFXPperMin * 60;

                    currentDEFLVL = ResourceDruids.this.skills.getRealLevel(Skills.DEFENSE);
                    DEFpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.DEFENSE);
                    g.setColor(new Color(102, 102, 255));
                    g.fill3DRect(187, 302, 329, 13, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentDEFLVL + "   " + " Xp Gained: " + currentDEF + "   " + "Xp/Hour: " + (int) DEFXPperHour + "   " + DEFpr + "%.", 190, (312));
                }
                if (!paintHP) {
                    currentHP = ResourceDruids.this.skills.getCurrentExp(Skills.CONSTITUTION) - startHP;
                    if (currentHP > 0) {
                        paintHP = true;
                    }
                } else {
                    currentHP = ResourceDruids.this.skills.getCurrentExp(Skills.CONSTITUTION) - startHP;
                    float HPXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentHP > 0) {
                        HPXPperSec = ((float) currentHP) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                    }
                    float HPXPperMin = HPXPperSec * 60;
                    float HPXPperHour = HPXPperMin * 60;
                    currentHPLVL = ResourceDruids.this.skills.getRealLevel(Skills.CONSTITUTION);
                    HPpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.CONSTITUTION);
                    g.setColor(new Color(255, 0, 102));
                    g.fill3DRect(187, 266, 329, 13, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentHPLVL + "   " + " Xp Gained: " + currentHP + "   " + "Xp/Hour: " + (int) HPXPperHour + "   " + HPpr + "%.", 190, (276));
                }
                if (!paintATT) {
                    currentATT = ResourceDruids.this.skills.getCurrentExp(Skills.ATTACK) - startATT;
                    if (currentATT > 0) {
                        paintATT = true;
                    }
                } else {
                    currentATT = ResourceDruids.this.skills.getCurrentExp(Skills.ATTACK) - startATT;
                    float ATTXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentATT > 0) {
                        ATTXPperSec = ((float) currentATT) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                    }
                    float ATTXPperMin = ATTXPperSec * 60;
                    float ATTXPperHour = ATTXPperMin * 60;
                    currentATTLVL = ResourceDruids.this.skills.getRealLevel(Skills.ATTACK);
                    ATTpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.ATTACK);
                    g.setColor(new Color(255, 51, 0));
                    g.fill3DRect(187, 278, 329, 13, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentATTLVL + "   " + " Xp Gained: " + currentATT + "   " + "Xp/Hour: " + (int) ATTXPperHour + "   " + ATTpr + "%.", 190, (288));

                }

                if (!paintRNG) {
                    currentRNG = ResourceDruids.this.skills.getCurrentExp(Skills.RANGE) - startRNG;
                    if (currentRNG > 0) {
                        paintRNG = true;
                    }
                } else {
                    currentRNG = ResourceDruids.this.skills.getCurrentExp(Skills.RANGE) - startRNG;
                    float RNGXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentRNG > 0) {
                        RNGXPperSec = ((float) currentRNG) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
                    }
                    float RNGXPperMin = RNGXPperSec * 60;
                    float RNGXPperHour = RNGXPperMin * 60;
                    currentRNGLVL = ResourceDruids.this.skills.getRealLevel(Skills.RANGE);
                    RNGpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.RANGE);
                    g.setColor(new Color(51, 153, 0));
                    g.fill3DRect(187, 314, 329, 13, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentRNGLVL + "   " + "Xp Gained: " + currentRNG + "   " + "Xp/Hour: " + (int) RNGXPperHour + "   " + RNGpr + "%.", 190, (324));
                }

                if (!paintMAG) {
                    currentMAG = ResourceDruids.this.skills.getCurrentExp(Skills.MAGIC) - startMAG;
                    if (currentMAG > 0) {
                        paintMAG = true;
                    }
                } else {
                    currentMAG = ResourceDruids.this.skills.getCurrentExp(Skills.MAGIC) - startMAG;
                    float MAGXPperSec = 0;
                    if ((minutes > 0 || hours > 0 || seconds > 0) && currentMAG > 0) {
                        MAGXPperSec = ((float) currentMAG) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                    }
                    float MAGXPperMin = MAGXPperSec * 60;
                    float MAGXPperHour = MAGXPperMin * 60;
                    currentMAGLVL = ResourceDruids.this.skills.getRealLevel(Skills.MAGIC);
                    MAGpr = ResourceDruids.this.skills.getPercentToNextLevel(Skills.MAGIC);
                    g.setColor(new Color(51, 0, 255));
                    g.fill3DRect(187, 327, 329, 12, true);
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.drawString("  Level: " + currentMAGLVL + "   " + " Xp Gained: " + currentMAG + "   " + "Xp/Hour: " + (int) MAGXPperHour + "   " + MAGpr + "%.", 190, (337));

                }
                g.setColor(new Color(210, 105, 30));
                g.fillRect(3, 266, 184, 73);
                if (startScript) {
                    g.setColor(Color.black);
                    g.drawString("Ownageful Druids" + " v" + version, 15, 277);
                    g.setColor(Color.white);
                    g.drawString("Time running: " + hours + ":" + minutes + ":" + seconds + ".", 15, 289);
                    g.drawString("Herbs Looted: " + herbsLooted, 15, 301);
                    g.drawString("Total Loot: " + totalLoot, 15, 313);
                    g.drawString("Loot Per Hour: " + (int) (profitPerSecond * 3600), 15, 325);
                    g.drawString("Bank Runs: " + bankRuns + " times.", 15, 337);
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    if (paintHP) {
                        g.setColor(new Color(255, 0, 102));
                        g.drawString("Hp", 164, (276));
                    }
                    if (paintATT) {
                        g.setColor(new Color(255, 51, 0));
                        g.drawString("Att", 164, (288));
                    }
                    if (paintSTR) {
                        g.setColor(new Color(255, 102, 0));
                        g.drawString("Str", 164, (300));
                    }
                    if (paintDEF) {
                        g.setColor(new Color(102, 102, 255));
                        g.drawString("Def", 164, (312));
                    }
                    if (paintRNG) {
                        g.setColor(new Color(51, 153, 0));
                        g.drawString("Rng", 164, (324));
                    }
                    if (paintMAG) {
                        g.setColor(new Color(51, 0, 255));
                        g.drawString("Mgc", 164, (336));
                    }

                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    g.setColor(Color.black);
                    g.fill3DRect(445, 4, 71, 14, false);
                    g.setColor(Color.green);
                    g.drawString(" Herb Count", 447, 15);


                    mx = mouse.getLocation().x;
                    my = mouse.getLocation().y;
                    g.setColor(Color.black);
                    g.fillRoundRect(345, 19, 171, 116, 15, 15);
                    g.setColor(Color.green);
                    for (int i = 0; i < counts.length; i++) {
                        if (i <= 8) {
                            g.drawString((hnames[i] + " : " + counts[i]), 350, (i + 1) * 13 + 15);
                        } else {
                            g.drawString((hnames[i] + " : " + counts[i]), 441, (i - 8) * 13 + 15);
                        }
                    }

                    cmx = mx;
                    cmy = my;
                    g.setColor(Color.black);
                    g.drawLine(cmx, cmy, 0, cmy);
                    g.drawLine(cmx, cmy, 516, cmy);
                    g.drawLine(cmx, cmy, cmx, 0);
                    g.drawLine(cmx, cmy, cmx, game.getBaseY());
                }
            }
        }
    }
    public void messageReceived(MessageEvent me) {
        String str = me.getMessage().toString();
        if (str.contains("already")) {
            nSleep = true;
        }
    }

    	public RSTile[] reversePath(final RSTile[] other) {
		final RSTile[] t = new RSTile[other.length];
		for (int i = 0; i < t.length; i++) {
			t[i] = other[other.length - i - 1];
		}
		return t;
	}

    private boolean inSpawns(RSTile l) {
        for (int i = 0; i < tils.length; i++) {
            if (l.equals(tils[i])) {
                return true;
            }
        }
        return false;
    }
    public class GUI extends javax.swing.JFrame {
        private Object jComboBox3;
        public GUI() {
            super();
            initComponents();
            setVisible(true);
        }
        private void initComponents() {

            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jComboBox2 = new javax.swing.JComboBox();
            jComboBox3 = new javax.swing.JComboBox();
            jLabel4 = new javax.swing.JLabel();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jRadioButton3 = new javax.swing.JRadioButton();
            jRadioButton4 = new javax.swing.JRadioButton();
            jRadioButton5 = new javax.swing.JRadioButton();
            jRadioButton6 = new javax.swing.JRadioButton();
            jRadioButton7 = new javax.swing.JRadioButton();
            jRadioButton8 = new javax.swing.JRadioButton();
            jRadioButton9 = new javax.swing.JRadioButton();
            jRadioButton10 = new javax.swing.JRadioButton();
            jRadioButton11 = new javax.swing.JRadioButton();
            jRadioButton13 = new javax.swing.JRadioButton();
            jRadioButton15 = new javax.swing.JRadioButton();
            jRadioButton16 = new javax.swing.JRadioButton();
            jRadioButton17 = new javax.swing.JRadioButton();
            jButton1 = new javax.swing.JButton();
            jLabel5 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setTitle("              Ownageful Resource Druids Killer - Settings");
            setAlwaysOnTop(true);

            jPanel1.setBackground(new java.awt.Color(0, 0, 0));
            jPanel1.setForeground(new java.awt.Color(0, 204, 51));

            jLabel1.setFont(new java.awt.Font("Croobie", 1, 24)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(204, 0, 0));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Ownageful Resource Druids Killer");

            jComboBox1.setBackground(new java.awt.Color(204, 0, 0));
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Tuna", "Salmon", "Trout"}));

            jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel2.setForeground(new java.awt.Color(204, 0, 0));
            jLabel2.setText("Food Type:");

            jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel3.setForeground(new java.awt.Color(204, 0, 0));
            jLabel3.setText("Withdraw:");

            jComboBox2.setBackground(new java.awt.Color(204, 0, 0));
            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"5", "10", "15", "20", "None"}));

            jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel4.setForeground(new java.awt.Color(204, 0, 0));
            jLabel4.setText("Select the loot:");

            jRadioButton1.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton1.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton1.setSelected(true);
            jRadioButton1.setText("Harrlander");

            jRadioButton2.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton2.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton2.setSelected(true);
            jRadioButton2.setText("Cadantine");

            jRadioButton3.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton3.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton3.setSelected(true);
            jRadioButton3.setText("Avantoe");

            jRadioButton4.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton4.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton4.setSelected(true);
            jRadioButton4.setText("Dwarf");

            jRadioButton5.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton5.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton5.setSelected(true);
            jRadioButton5.setText("Snapdragon");

            jRadioButton6.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton6.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton6.setSelected(true);
            jRadioButton6.setText("Ranarr");

            jRadioButton7.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton7.setForeground(new java.awt.Color(0, 204, 0));
            jRadioButton7.setSelected(true);
            jRadioButton7.setText("Irit");

            jRadioButton8.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton8.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton8.setSelected(true);
            jRadioButton8.setText("Kwuarm");

            jRadioButton9.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton9.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton9.setSelected(true);
            jRadioButton9.setText("Ladantyne");

            jRadioButton10.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton10.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton10.setSelected(true);
            jRadioButton10.setText("Toadflax");

            jRadioButton11.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton11.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton11.setText("Marrentil");

            jRadioButton13.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton13.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton13.setSelected(true);
            jRadioButton13.setText("Laws");

            jRadioButton15.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton15.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton15.setSelected(true);
            jRadioButton15.setText("Mith Bolts");

            jRadioButton16.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton16.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton16.setText("Tarromin");

            jRadioButton17.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton17.setForeground(new java.awt.Color(0, 204, 51));
            jRadioButton17.setSelected(true);
            jRadioButton17.setText("Natures");

            jButton1.setBackground(new java.awt.Color(0, 0, 0));
            jButton1.setForeground(new java.awt.Color(204, 0, 0));
            jButton1.setText("Start!");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jLabel5.setForeground(new java.awt.Color(255, 51, 0));
            jLabel5.setText("Select desired settings and Start :)");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jRadioButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jRadioButton2).addComponent(jRadioButton7))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jRadioButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)))).addContainerGap(26, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE).addContainerGap()));
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jLabel4)).addGap(4, 4, 4).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(21, 21, 21)).addGroup(jPanel1Layout.createSequentialGroup().addGap(44, 44, 44).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel1Layout.createSequentialGroup().addGap(33, 33, 33).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addGap(11, 11, 11).addComponent(jRadioButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jRadioButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jLabel5)).addGap(54, 54, 54)));

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE));

            pack();
            jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButton1ActionPerformed(e);
                }
            });
        }
        private void jButton1ActionPerformed(ActionEvent e) {

            int foodIndex = jComboBox1.getSelectedIndex();
            if (foodIndex == 0) {
                food = foodIDS[0];
                foodName = "Tuna";
            } else if (foodIndex == 1) {
                food = foodIDS[1];
                foodName = "Salmon";
            } else if (foodIndex == 2) {
                food = foodIDS[2];
                foodName = "Trout";
            }

            int withdrawIndex = jComboBox2.getSelectedIndex();
            if (withdrawIndex == 0) {
                withdrawAmount = 5;
            } else if (withdrawIndex == 1) {
                withdrawAmount = 10;
            } else if (withdrawIndex == 2) {
                withdrawAmount = 15;
            } else if (withdrawIndex == 3) {
                withdrawAmount = 20;
            } else if (withdrawIndex == 4) {
                usingFood = false;
                food = -1;
            }

            int counter = 3;

            if (jRadioButton1.isSelected()) {
                counter++;
            }
            if (jRadioButton2.isSelected()) {
                counter++;
            }
            if (jRadioButton3.isSelected()) {
                counter++;
            }
            if (jRadioButton4.isSelected()) {
                counter++;
            }
            if (jRadioButton5.isSelected()) {
                counter++;
            }
            if (jRadioButton6.isSelected()) {
                counter++;
            }
            if (jRadioButton7.isSelected()) {
                counter++;
            }
            if (jRadioButton8.isSelected()) {
                counter++;
            }
            if (jRadioButton9.isSelected()) {
                counter++;
            }
            if (jRadioButton10.isSelected()) {
                counter++;
            }
            if (jRadioButton11.isSelected()) {
                counter++;
            }
            if (jRadioButton17.isSelected()) {
                counter++;
            }
            if (jRadioButton13.isSelected()) {
                counter++;
            }
            if (jRadioButton16.isSelected()) {
                counter++;
            }
            if (jRadioButton15.isSelected()) {
                counter++;
            }
            if (food != -1) {
                counter++;
            }

            if (pickUpArrows) {
                counter++;
            }

            customLoot = new int[counter];
            int index = 0;

            if (jRadioButton1.isSelected()) {
                index++;
                customLoot[index - 1] = (205);
            }

            if (jRadioButton2.isSelected()) {
                index++;
                customLoot[index - 1] = (215);
            }
            if (jRadioButton3.isSelected()) {
                index++;
                customLoot[index - 1] = (211);
            }
            if (jRadioButton4.isSelected()) {
                index++;
                customLoot[index - 1] = (217);
            }
            if (jRadioButton5.isSelected()) {
                index++;
                customLoot[index - 1] = (3051);
            }
            if (jRadioButton6.isSelected()) {
                index++;
                customLoot[index - 1] = (207);
            }
            if (jRadioButton7.isSelected()) {
                index++;
                customLoot[index - 1] = (209);
            }
            if (jRadioButton8.isSelected()) {
                index++;
                customLoot[index - 1] = (213);
            }
            if (jRadioButton9.isSelected()) {
                index++;
                customLoot[index - 1] = (2485);
            }
            if (jRadioButton10.isSelected()) {
                index++;
                customLoot[index - 1] = (3049);
            }
            if (jRadioButton11.isSelected()) {
                index++;
                customLoot[index - 1] = (201);
            }
            if (jRadioButton17.isSelected()) {
                index++;
                customLoot[index - 1] = (561);
            }
            if (jRadioButton13.isSelected()) {
                index++;
                customLoot[index - 1] = (563);
            }
            if (jRadioButton16.isSelected()) {
                index++;
                customLoot[index - 1] = (203);
            }
            if (jRadioButton15.isSelected()) {
                index++;
                customLoot[index - 1] = (9142);
            }

            index++;
            customLoot[index - 1] = (1619);
            index++;
            customLoot[index - 1] = (1617);
            index++;
            customLoot[index - 1] = (1621);

            if (food != -1 && !pickUpArrows) {
                customLoot[index] = food;
            } else if (food != -1 && pickUpArrows) {
                customLoot[index - 1] = arrowPickupID;
                customLoot[index] = food;
            } else if (food == -1 && pickUpArrows) {
                customLoot[index] = arrowPickupID;
            }

            gui.setVisible(false);

            startScript = true;
        }
        private javax.swing.JButton jButton1;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JComboBox jComboBox2;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JRadioButton jRadioButton1;
        private javax.swing.JRadioButton jRadioButton10;
        private javax.swing.JRadioButton jRadioButton11;
        private javax.swing.JRadioButton jRadioButton13;
        private javax.swing.JRadioButton jRadioButton15;
        private javax.swing.JRadioButton jRadioButton16;
        private javax.swing.JRadioButton jRadioButton17;
        private javax.swing.JRadioButton jRadioButton2;
        private javax.swing.JRadioButton jRadioButton3;
        private javax.swing.JRadioButton jRadioButton4;
        private javax.swing.JRadioButton jRadioButton5;
        private javax.swing.JRadioButton jRadioButton6;
        private javax.swing.JRadioButton jRadioButton7;
        private javax.swing.JRadioButton jRadioButton8;
        private javax.swing.JRadioButton jRadioButton9;
    }
}
