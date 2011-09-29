import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import org.rsbot.Configuration;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Equipment;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Game.ChatMode;
import org.rsbot.script.methods.Prayer;
import org.rsbot.script.methods.Prayer.ProtectPrayer;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.util.WindowUtil;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;
@ScriptManifest(authors = {"Ownageful"}, name = "Ownageful's Frost Dragon Killer", version = 2.06, description = "FDK Pro. Settings in GUI")
public class OwnageFDK extends Script implements PaintListener, MessageListener, MouseListener {
    private String version = "2.06";
    private int[] prayerPotions = {2434, 139, 141, 143};
    private int[] strengthPots = {2440, 157, 159, 161};
    private int[] attackPots = {2436, 145, 147, 149};
    private int[] antiPots = {2452, 2454, 2456, 2458};
    private int[] rangedPots = {2444, 169, 171, 173};
    private int[] superantiPots = {15304, 15305, 15306, 15307};
    private int[] extremeRangePots = {15324, 15325, 15326, 15327};
    private int[] extremeattackPots = {15308, 15309, 15310, 15311};
    private int[] extremestrengthPots = {15312, 15313, 15314, 15315};
    private int[] defencePots = {2442, 163, 165, 167};
    private int[] bobPouchIDS = {12087, 12007, 12031, 12093};
    private int[] potionAmounts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] drop = {565, 560, 563, 1462, 1621, 1619, 1617,
        1623, 229, 13280, 1365, 892, 995, 1355,
        1069, 555, 1179, 2485, 3051, 3049, 563,
        561, 9142, 203, 449, 1123, 1199, 8836, 1445, 218, 204, 226, 214};
    private int[] cannonParts = {6, 8, 10, 12};
    private int[] messedUpCannonParts = {7, 8, 9};
    private int[] dragons = {11635, 11636, 11634, 11633, 51};
    private int[] gloryCharges = {1712, 1710, 1708, 1706};
    private int[] emptyGlory = {1704};
    private int[] lootIDs;
    private boolean[] takingPotions = {false, false, false, false, false, false, false, false, false, false};
    private int[][] potionArrays = {antiPots, rangedPots, attackPots, strengthPots, prayerPotions, superantiPots, extremeRangePots, extremeattackPots, extremestrengthPots, defencePots};
    private boolean nBank = false, useScroll = false, doneWithCannon = false, tabTaken = false, nAnti = false, needNewGlory = false;
    private String[] lootNames;
    private int[] prices;
    private boolean summonFull = false, needToPickupCannon = false, drinkFirstAntiFire = true, needToRecoverCannon = false, needCannonFromDwarf = false, useQuickPrayer = false, needToFill = false, usingSummon = false, paint = true, usingCannon = false, usingFood = false;
    private ArrayList<Item> loots = new ArrayList<Item>();
    private ArrayList<Integer> charmsToDrop = new ArrayList<Integer>();
    private int[] charmsToDropArray;
    private int cannonBallsToTake = 0;
    private int cannonBalls = 2, food = -1, finalPouch = -1, foodAmount = -1, eatAt = 40, tab = -1, visage = 0, effigy = 0;
    // DRAYNOR BANKING METHOD VARIABLES
    private RSArea draynorBank = new RSArea(new RSTile(3092, 3240), new RSTile(3097, 3246));
    private RSTile draynorBankCentralTile = new RSTile(3092, 3243);
    private RSArea draynorTeleportArea = new RSArea(new RSTile(3093, 3247), new RSTile(3113, 3255));
    private int draynorBanker = 2015, scrollsToTake = 0;
    // FALADOR BANKING METHOD VARIABLES
    private RSArea faladorBank = new RSArea(new RSTile(2943, 3367), new RSTile(2950, 3372));
    private RSArea faladorTeleportArea = new RSArea(new RSTile(2961, 3376), new RSTile(2969, 3384));
    private int fallyTabs = 8009, houseTabs = 8013, bankIndex = 0;
    private int fallyBanker = 11758;
    private RSTile faladorBankCentralTile = new RSTile(2946, 3369);
    // EDGEVILLE BANKING METHOD VARIABLES
    private RSArea edgevilleBank = new RSArea(new RSTile(3091, 3488), new RSTile(3099, 3499));
    private RSArea edgevilleTeleportArea = new RSArea(new RSTile(3084, 3490), new RSTile(3090, 3499));
    private RSArea edgevilleCityArea = new RSArea(new RSTile(3091, 3485), new RSTile(3143, 3520));
    private RSTile edgevilleBankCentralTile = new RSTile(3095, 3497);
    private int edgevilleBanker = 26972;
    // Final BANKING METHOD VARIABLES
    private RSArea finalBankArea;
    private RSArea finalTeleportArea;
    private RSTile finalBankCentralTile;
    private int finalTeleportAnimation;
    private int finalBanker;
    private RSTile outsideBankTile;
    private String gloryAction = "";
    // The north section of the frost area
    private RSArea northFrosts = new RSArea(new RSTile(1293, 4511), new RSTile(1339, 4541));
    // Nulodian variables
    private RSArea dwarfHouse = new RSArea(new RSTile(3008, 3452), new RSTile(3014, 3454));
    private RSNPC dwarf;
    private int dwarfID = 209;
    private RSTile outsideDwardHouse = new RSTile(3015, 3453);
    private RSObject dwarfDoor;
    private int dwardDoorID = 3, cx = 0, cy = 0;
    // The path from draynor bank to the dungeon trapdoor
    private RSTile[] draynorBankToTrap = {
        new RSTile(3085, 3251), new RSTile(3076, 3257),
        new RSTile(3074, 3269), new RSTile(3067, 3273),
        new RSTile(3057, 3267), new RSTile(3046, 3265),
        new RSTile(3038, 3254), new RSTile(3027, 3244),
        new RSTile(3015, 3242), new RSTile(3008, 3231),
        new RSTile(3006, 3220), new RSTile(3003, 3214), new RSTile(3002, 3202), new RSTile(3005, 3191),
        new RSTile(3007, 3180), new RSTile(3008, 3176), new RSTile(3017, 3164),
        new RSTile(3012, 3157), new RSTile(3009, 3150)};
    private RSTile[] fairyRingToTrap = {new RSTile(2996, 3114), new RSTile(2999, 3123), new RSTile(3001, 3133), new RSTile(3002, 3142), new RSTile(3009, 3151)};
    private RSTile[] finalPathToTrap;
    private RSTile[] houseToTrap = {
        new RSTile(2963, 3223), new RSTile(2972, 3219),
        new RSTile(2976, 3208), new RSTile(2985, 3203),
        new RSTile(2993, 3194), new RSTile(3002, 3191),
        new RSTile(3005, 3191), new RSTile(3007, 3180),
        new RSTile(3008, 3176), new RSTile(3017, 3164),
        new RSTile(3012, 3157), new RSTile(3009, 3150)};
    private RSTile[] edgebankToRing = {
        new RSTile(3099, 3497), new RSTile(3103, 3503),
        new RSTile(3112, 3508), new RSTile(3120, 3513),
        new RSTile(3128, 3516), new RSTile(3128, 3516),
        new RSTile(3132, 3507), new RSTile(3129, 3498)};
    // The path from trapdoor to frosts dungeon door
    private RSTile[] trapToDungeon = {
        new RSTile(2999, 9549), new RSTile(2994, 9556), new RSTile(2993, 9563),
        new RSTile(2994, 9573), new RSTile(2999, 9579), new RSTile(3004, 9579), new RSTile(3018, 9579),
        new RSTile(3030, 9583), new RSTile(3034, 9591),
        new RSTile(3033, 9591), new RSTile(3033, 9599)};
    private RSTile[] cannonSpots = {
        new RSTile(1310, 4523), new RSTile(1311, 4531), new RSTile(1320, 4534),
        new RSTile(1330, 4523), new RSTile(1319, 4518), new RSTile(1331, 4530), new RSTile(1301, 4529)};
    private RSObject trapdoor, dungeonGate, fullCannon, fairyRing;
    private RSTile cannonSpot;
    private RSGroundItem lootItem;
    private RSNPC dragon;
    private int trapdoorID = 9472, dungeonGateID = 52859, fullCannonID = 6, fairyRingID = 14097, cannonRecoverCounter = 0;
    private String status = "";
    private int totalLoot = 0;
    private TimeListener timeListener;
    private Image paintImage, paintImage1, cursor;
    private Point mouseLocation;
    private Properties props;
    Timer fillTimer = new Timer(120000);
    Timer replaceTimer = new Timer(840000);
    private final Filter<RSNPC> frostDragonFilter = new Filter<RSNPC>() {
        @Override
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equals("Frost dragon") && northFrosts.contains(npc.getLocation())
                        && !npc.isInCombat() && (npc.getInteracting() == null || npc.getInteracting() == getMyPlayer()) && npc.getAnimation() != 13153);
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    private final Filter<RSNPC> firstDragonFilter = new Filter<RSNPC>() {
        @Override
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equals("Frost dragon") && npc.getInteracting().equals(getMyPlayer()));
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    private final Filter<RSGroundItem> lootfilter = new Filter<RSGroundItem>() {
        @Override
        public boolean accept(RSGroundItem obj) {
            try {
                return isValidLoot(obj.getItem().getID()) && northFrosts.contains(obj.getLocation());
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    private final Filter<RSObject> cannonfilter = new Filter<RSObject>() {
        @Override
        public boolean accept(RSObject obj) {
            try {
                return obj.getID() == 6 && northFrosts.contains(obj.getLocation());
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    FDKGUI gui;
    @Override
    public boolean onStart() {
        return (game.isLoggedIn() && createAndWaitForGUI());
    }
    @Override
    public void onFinish() {
        if (fullCannon != null) {
            fullCannon.interact("Pick-up");
            sleep(random(1000, 1100));
            waitToMove();
        }
        teleport();
        env.takeScreenshot(true);
        super.stopScript();
    }
    @Override
    public int loop() {
        if (!needToFill) {
            fillTimer.reset();
        }
        try {
            manageFood();
            gameChatModeColor();
            if (nBank && (inTeleportLocation() || !areInDesiredBank())) {
                if (cannonPlanted() && usingCannon) {
                    needToPickupCannon = true;
                }
                if (dwarfHouse.contains(getMyPlayer().getLocation())) {
                    teleport();
                    return 1000;
                }
                status = "Banking";
                closePrayers();
                try {
                    walking.walkTo(finalBankCentralTile);
                    sleep(random(700, 1200));
                } catch (NullPointerException e) {
                    return 100;
                }
            } else if (areInDesiredBank()) {
                closePrayers();
                if (bankIndex != 1) {
                    if (!chargedGloryEquipped()) {
                        needNewGlory = true;
                    }
                }
                if (readyToLeave()) {
                    bank.close();
                    if (bankIndex != 1) {
                        if (inventory.contains(emptyGlory[0])) {
                            RSObject b = objects.getNearest(finalBanker);
                            if (b != null) {
                                b.interact("Use-quickly");
                                sleep(random(3000, 3500));
                                bank.deposit(emptyGlory[0], 1);
                            }
                        }
                    }
                    nBank = false;
                    if (!needCannonFromDwarf) {
                        if (bankIndex != 1) {
                            walking.walkTo(outsideBankTile);
                            sleep(random(3000, 4000));
                        } else {
                            try {
                                inventory.getItem(houseTabs).interact("Break");
                                sleep(random(4000, 5000));
                            } catch (NullPointerException e) {
                            }
                        }
                    } else {
                        walking.walkTileMM(outsideBankTile);
                        sleep(random(3000, 4000));
                    }
                } else {
                    RSObject b = objects.getNearest(finalBanker);
                    if (b != null) {
                        camera.turnTo(b);
                        if (!b.isOnScreen()) {
                            walking.walkTileMM(b.getLocation());
                        } else {
                            b.interact("Use-quickly");
                            sleep(random(2000, 2200));
                            if (bank.isOpen()) {
                                if (!tabTaken) {
                                    sleep(random(1000, 1500));
                                    tab = bank.getCurrentTab();
                                    tabTaken = true;
                                } else {
                                    openCorrectTab();
                                }
                                try {
                                    if (!inventory.containsOneOf(cannonParts)) {
                                        bank.depositAll();
                                    } else {
                                        bank.depositAllExcept(cannonParts);
                                    }
                                    if (summoning.isFamiliarSummoned()) {
                                        bank.depositAllFamiliar();
                                    }
                                    if (needNewGlory) {
                                        doGloryRemoval();
                                        bank.depositAll();
                                    }
                                    summonFull = false;
                                    sleep(random(1000, 2000));
                                    openCorrectTab();
                                    if (needCannonFromDwarf) {
                                        withdrawRequiredGlory();
                                        openCorrectTab();
                                        equipChargedGlory();
                                        sleep(random(700, 800));
                                    } else {
                                        withdrawRequiredGlory();
                                        openCorrectTab();
                                        openCorrectTab();
                                        withdrawRequiredPotions();
                                        openCorrectTab();
                                        withdrawRequiredSummonItems();
                                        openCorrectTab();
                                        withdrawRequiredCannonItems();
                                        openCorrectTab();
                                        withdrawRequiredFood();
                                        equipChargedGlory();
                                        sleep(random(700, 800));
                                    }
                                } catch (NullPointerException k) {
                                    return 100;
                                }
                            }
                        }

                    }
                }
            } else if (isInMainLand()) {
                manageFood();
                closePrayers();
                if (needCannonFromDwarf) {
                    RSTile[] dwarfWeb = web.generateTilePath(getMyPlayer().getLocation(), outsideDwardHouse);
                    if (!dwarfHouse.contains(getMyPlayer().getLocation())) {
                        walkPathAndInteractWithObject(dwarfWeb, dwarfDoor, dwardDoorID, "Open");
                        return 800;
                    } else {
                        if (interfaces.canContinue()) {
                            log("Clicking continue");
                            interfaces.clickContinue();
                            return 2000;
                        } else if (!cannonPlanted()) {
                            log("We've received cannon!");
                            needCannonFromDwarf = false;
                            usingCannon = true;
                            nBank = true;
                        } else {
                            dwarf = npcs.getNearest(dwarfID);
                            if (dwarf != null) {
                                dwarf.interact("Replace");
                                return 4000;
                            } else {
                                return 100;
                            }
                        }
                    }
                } else {
                    if (bankIndex == 2 && isInEdgeville()) {
                        walkPathAndInteractWithObject(edgebankToRing, fairyRing, fairyRingID, "Use");
                    } else {
                        walkPathAndInteractWithObject(finalPathToTrap, trapdoor, trapdoorID, "Climb-down");
                    }
                }
            } else if (isInZanaris()) {
                doRingBusiness();
            } else if (isInSecondFloor()) {
                closePrayers();
                walkPathAndInteractWithObject(trapToDungeon, dungeonGate, dungeonGateID, "Enter");
            } else if (isInDungeon()) {
                if (drinkFirstAntiFire) {
                    nAnti = true;
                    walking.walkTo(northFrosts.getCentralTile());
                    sleep(random(1500, 2500));
                    if (usingCannon) {
                        needToFill = true;
                        fillTimer.reset();
                        if (cannonPlanted()) {
                            fullCannon = objects.getTopAt(new RSTile(cx, cy));
                        }
                    }
                    drinkFirstAntiFire = false;
                }
                manageTeleportaion();
                managePrayers();
                manageFood();
                managePotting();
                manageLooting();
                manageSummoning();
                manageFighting();
                manageCannon();
            }
            minorChecks();
            return 20;
        } catch (NullPointerException e) {
            manageTeleportaion();
            manageFood();
            return 10;
        }
    }
    private boolean needToTele() {
        return (((summonFull || !usingSummon) && inventory.isFull() && inventory.getCount(food) == 0)
                || (inventory.getCount(food) == 0 && getMyPlayer().getHPPercent() <= 70));
    }
    private boolean needToSummon() {
        try {
            return (usingSummon && !summoning.isFamiliarSummoned());
        } catch (NullPointerException e) {
            return false;
        }
    }
    //
    private boolean needToRenew() {
        try {
            return (usingSummon && summoning.isFamiliarSummoned() && summoning.getTimeLeft() < 2.00);
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean canSummon() {
        try {
            return inventory.contains(finalPouch) && summoning.getSummoningPoints() >= 10;
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void doSummon() {
        try {
            inventory.getItem(finalPouch).interact("Summon");
        } catch (NullPointerException e) {
        }
    }
    private void doRenew() {
        try {
            summoning.doRenewFamiliar();

        } catch (NullPointerException e) {
        }
    }
    private void doWinterScroll() {
        summoning.doAction("Cast");
        sleep(random(800, 1200));
        RSItem toStore = null;
        try {
            toStore = inventory.getItem(lootIDs[0], lootIDs[1], lootIDs[2], lootIDs[3], lootIDs[4], lootIDs[5], lootIDs[6]);
            toStore.doClick(true);
            sleep(random(1200, 1800));
        } catch (Exception e) {
        }
    }
    private void storeSummon() {
        RSItem toStore = null;
        try {
            toStore = inventory.getItem(lootIDs[0], lootIDs[1], lootIDs[2], lootIDs[3], lootIDs[4], lootIDs[5], lootIDs[6]);
        } catch (Exception e) {
        }
        if (toStore != null) {
            if (toStore.interact("Use")) {
                try {
                    mouse.click(summoning.getFamiliar().getNPC().getModel().getCentralPoint(), true);
                    sleep(random(800, 1100));
                } catch (NullPointerException e) {
                }
            }
        }
    }
    private boolean needToDrinkPray() {
        try {
            return prayer.getPrayerLeft() <= 12;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    private boolean setMagicPrayer() {
        return prayer.protectFrom(ProtectPrayer.MAGE);
    }
    private boolean isProtected() {
        if (prayer.isCursing()) {
            return prayer.isPrayerOn(Prayer.Curses.DEFLECT_MAGIC);
        } else {
            return prayer.isPrayerOn(Prayer.Normal.PROTECT_FROM_MAGIC);
        }
    }
    private boolean drinkPrayerPotion() {
        RSItem prayerPotion = inventory.getItem(prayerPotions);
        return prayerPotion != null && prayerPotion.interact("Drink");
    }
    private boolean isInDungeon() {
        RSObject ringZanaris = objects.getNearest(12128);
        RSNPC drag = npcs.getNearest(dragons);
        return (Math.floor(getMyPlayer().getLocation().getX() / 1000) == 1.0)
                && (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0) && ringZanaris == null && drag != null;
    }
    private boolean isInZanaris() {
        RSObject ringZanaris = objects.getNearest(12128);
        return (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0)
                && ringZanaris != null;
    }
    private boolean isInMainLand() {
        return (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 3.0);
    }
    private boolean isInSecondFloor() {
        return (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 9.0);
    }
    private void calculatePrices() {
        Thread priceThread = new Thread() {
            @Override
            public void run() {
                paintImage = getImage("http://www.ownagebots.com/frostPaint.png");
                paintImage1 = getImage("http://www.ownagebots.com/frostPaint2.png");
                cursor = getImage("http://www.ownagebots.com/obCursor.png");
                log("Loading guide prices for profitable loots.");
                for (int b = 0; b < lootIDs.length; b++) {
                    try {
                        prices[b] = grandExchange.lookup(lootIDs[b]).getGuidePrice();
                        prices[b] = (int) (prices[b] * 1.05);
                    } catch (NullPointerException e) {
                        prices[b] = 0;
                    }
                    log("" + lootNames[b] + " [" + lootIDs[b] + "] guide price is " + prices[b]);
                }
                this.interrupt();
            }
        };
        priceThread.start();
    }
    private boolean isValidLoot(int iD) {
        for (int i = 0; i < lootIDs.length; i++) {
            if (lootIDs[i] == iD) {
                return true;
            }
        }
        return false;
    }
    private void teleport() {
        if (isInDungeon() || dwarfHouse.contains(getMyPlayer().getLocation())) {
            while (getMyPlayer().getAnimation() != finalTeleportAnimation) {
                manageFood();
                managePrayers();
                if (inTeleportLocation()) {
                    sleep(random(2000, 3000));
                    nBank = true;
                    drinkFirstAntiFire = true;
                    doneWithCannon = false;
                    break;
                }
                try {
                    if (bankIndex == 1) {
                        inventory.getItem(fallyTabs).interact("Break");
                        sleep(random(1200, 1300));
                    } else {
                        game.openTab(5);
                        if (interfaces.get(Equipment.INTERFACE_EQUIPMENT).getComponent(Equipment.NECK).interact(gloryAction)) {
                            sleep(random(1200, 1800));
                        }
                    }
                } catch (NullPointerException e) {
                    break;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            sleep(random(4000, 5000));
            log("Teleported succesfully");
            nBank = true;
            drinkFirstAntiFire = true;
            doneWithCannon = false;
        } else {
            nBank = true;
            drinkFirstAntiFire = true;
            doneWithCannon = false;
        }
    }
    private void minorChecks() {
        setRun();
        closeInterface();
        cancelSelectedItem();
        dropShit();
        camera.setPitch(false);
    }
    private void manageSummoning() {
        if (usingSummon) {
            if (needToSummon()) {
                if (canSummon()) {
                    doSummon();
                }
            } else if (needToRenew()) {
                doRenew();
            }
        }
    }
    private void closeInterface() {
        if (interfaces.get(671).isValid()) {
            if (interfaces.get(671).getComponent(13).isValid()) {
                interfaces.get(671).getComponent(13).doClick();
            }
        }
    }
    private void cancelSelectedItem() {
        if (inventory.isItemSelected()) {
            inventory.getSelectedItem().interact("Cancel");
        }
    }
    private void setRun() {
        if (walking.getEnergy() > (random(5, 30)) && !walking.isRunEnabled()) {
            walking.setRun(true);
            sleep(random(1200, 1500));
        }
    }
    public boolean openBankTab(int tab) {
        if (!bank.isOpen() || (63 - (tab * 2) < 47)) {
            return false;
        }
        try {
            return bank.getInterface().getComponent(63 - (tab * 2)).doClick();
        } catch (NullPointerException e) {
            return false;
        }
    }
    public boolean didPot(int[] pots) {
        if (inventory.containsOneOf(pots)) {
            if (inventory.getItem(pots).interact("Drink")) {
                return true;
            }
        }
        return false;
    }
    public int gap(int[] pots) {
        if (bank.isOpen()) {
            for (int i = 0; i < 4; i++) {
                if (bank.getCount(pots[i]) > 0) {
                    return pots[i];
                }
            }
        }
        return 0;
    }
    public void onRepaint(Graphics g) {
        if (fullCannon != null) {
            Point cannonPoint = calc.tileToMinimap(fullCannon.getLocation());
            g.setColor(Color.magenta);
            g.fillRect(cannonPoint.x, cannonPoint.y, 4, 4);
            g.drawString(replaceTimer.toRemainingString(), cannonPoint.x + 4, cannonPoint.y + 4);
            g.drawString(fillTimer.toRemainingString(), cannonPoint.x + 12, cannonPoint.y + 12);
        }
        mouseLocation = mouse.getLocation();
        g.drawImage(cursor, mouseLocation.x - 2, mouseLocation.y, null);
        if (paint) {
            g.drawImage(paintImage, 2, 290, null);
            g.setColor(Color.PINK);
            g.drawString(timeListener.getRuntimeString(), 431, 366);
            g.drawString(totalLoot + "", 431, 390);
            g.drawString("" + timeListener.calcPerHour((long) totalLoot), 431, 404);
            g.drawString(version, 431, 427);
            g.setColor(Color.red);
            g.drawString(visage + "", 332, 458);
            g.setColor(Color.blue);
            g.drawString(effigy + "", 390, 458);
        } else {
            g.drawImage(paintImage1, 411, 432, null);
        }
    }
    public void messageReceived(MessageEvent me) {
        String str = me.getMessage();
        int id = me.getID();
        if (str.contains("dragonfire is about to") && (id == 0 || id == 109 || id == -1)) {
            nAnti = true;
        } else if (str.contains("Your familiar cannot") && usingSummon && (id == 0 || id == 109 || id == -1)) {
            summonFull = true;
        } else if (str.contains("cannon has decayed!") && usingCannon && (id == 0 || id == 109 || id == -1)) {
            usingCannon = false;
            needToRecoverCannon = false;
            needCannonFromDwarf = true;
        } else if (str.contains("enough space to set") && usingCannon && (id == 0 || id == 109 || id == -1)) {
            fullCannon = null;
        } else if (str.contains("Your cannon is out") && usingCannon && (id == 0 || id == 109 || id == -1)) {
            needToFill = true;
            fillTimer.reset();
        } else if (str.contains("top up") && usingCannon && (id == 0 || id == 109 || id == -1)) {
            needToFill = false;
            fillTimer.reset();
        } else if (str.contains("Your cannon is already") && usingCannon && (id == 0 || id == 109 || id == -1)) {
            needToFill = false;
            fillTimer.reset();
        } else if (str.contains("fiery breath") && (id == 0 || id == 109 || id == -1)) {
            nAnti = true;
        } else if (str.contains("dragonfire has run") && (id == 0 || id == 109 || id == -1)) {
            nAnti = true;
        } else if (str.contains("antifire potion.") && (id == 0 || id == 109 || id == -1)) {
            nAnti = false;
        }
    }
    private boolean walkPath(RSTile[] path) {
        if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) <= 1) {
            return walkPathMM(path);
        }
        return false;
    }
    public boolean walkPathMM(final RSTile[] path) {
        return walkPathMM(path, 16);
    }
    public boolean walkPathMM(final RSTile[] path, final int maxDist) {
        return walkPathMM(path, maxDist, 0, 0);
    }
    public boolean walkPathMM(final RSTile[] path, final int randX, final int randY) {
        return walkPathMM(path, 16, randX, randY);
    }
    public boolean walkPathMM(final RSTile[] path, final int maxDist, final int randX, final int randY) {
        try {
            final RSTile next = nextTile(path, maxDist);
            return next != null && walking.walkTileMM(next, randX, randY);
        } catch (final Exception e) {
            return false;
        }
    }
    public RSTile nextTile(final RSTile path[]) {
        return nextTile(path, 17);
    }
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
    private boolean createAndWaitForGUI() {
        if (SwingUtilities.isEventDispatchThread()) {

            log("Creating new gui");
            gui = new FDKGUI();
            log("Setting it visible gui");
            gui.setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        log("Creating new gui");
                        gui = new FDKGUI();
                        log("Setting it visible gui");
                        gui.setVisible(true);
                    }
                });
            } catch (InvocationTargetException ite) {
                log.severe("Error loading the properties, please go to Documents/RSBot/Cache/Scripts and delete frostSettings.props file, and start again.");
                return false;
            } catch (InterruptedException ie) {

                log("2");
                return false;
            }
        }
        sleep(100);
        mouse.setSpeed(random(6, 7));
        timeListener = new TimeListener();
        while (gui.isVisible()) {
            sleep(100);
        }
        gui.setProperties();
        calculatePrices();
        for (int i = 0; i < 1; i++) {
            game.setChatOption(Game.CHAT_OPTION_GAME, ChatMode.VIEW);
        }
        combat.setAutoRetaliate(true);
        return true;
    }
    private boolean readyToLeave() {
        return (haveTeleportationDevice() && needCannonFromDwarf && inventory.getCount() <= 6)
                || (haveTeleportationDevice() && cannonSupplied() && foodSupplied() && potionsSupplied() && !needCannonFromDwarf);
    }
    private boolean cannonSupplied() {
        boolean result = !usingCannon || (cannonBallsAmount() >= cannonBallsToTake);
        if (!result) {
            log("We dont have the required cannon balls , restarting banking method.");
        }
        return result;
    }
    private boolean potionsSupplied() {
        for (int i = 0; i < potionAmounts.length; i++) {
            if (inventory.getCount(potionArrays[i]) < potionAmounts[i]) {
                log("We dont have the required potions: " + potionArrays[i].toString() + ". Resarting banking method.");
                return false;
            }
        }
        return true;
    }
    private int getMenuIndex(String str) {
        String[] actions = menu.getOptions();
        for (int i = 0; i < actions.length; i++) {
            log(actions[i] + ", at index: " + i);
            if (actions[i].contains(str) || actions[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }
    private boolean foodSupplied() {
        boolean result = !usingFood || inventory.contains(food);
        if (!result) {
            log("We dont have the required food in inventory, restarting banking method.");
        }
        return result;
    }
    private boolean chargedGloryEquipped() {
        return equipment.containsOneOf(gloryCharges);
    }
    private void equipChargedGlory() {
        if (bankIndex != 1) {
            if (bank.close()) {
                try {
                    inventory.getItem(gloryCharges).interact("Wear");
                    sleep(random(800, 1000));
                } catch (NullPointerException e) {
                }
            }
        }
    }
    private void withdrawRequiredPotions() {
        for (int i = 0; i < takingPotions.length; i++) {

            if (takingPotions[i]) {
                bank.withdraw(gap(potionArrays[i]), potionAmounts[i]);
                sleep(random(500, 700));
            }
        }
    }
    private void withdrawRequiredSummonItems() {
        if (usingSummon) {
            if (!inventory.contains(finalPouch)) {
                if (bank.getItem(finalPouch) == null) {
                    openCorrectTab();
                } else {
                    bank.withdraw(finalPouch, 1);
                    sleep(random(800, 1000));
                    if (useScroll) {
                        bank.withdraw(12435, scrollsToTake);
                        sleep(random(800, 1000));
                    }
                }
            }
        }
    }
    private void withdrawRequiredFood() {
        if (usingFood) {
            for (int i = 0; i < 4; i++) {
                if (inventory.getCount(food) < foodAmount) {
                    bank.withdraw(food, foodAmount);
                    sleep(random(1800, 1900));
                }
            }
        }
    }
    private void withdrawRequiredCannonItems() {
        if (usingCannon) {
            for (int i = 0; i < 5; i++) {
                if (!inventory.contains(cannonBalls)) {
                    bank.withdraw(cannonBalls, cannonBallsToTake);
                    sleep(random(3000, 3500));
                }
            }
        }
    }
    private void withdrawRequiredGlory() {
        if (bankIndex == 1) {
            bank.withdraw(houseTabs, 1);
            sleep(random(1000, 1200));
            if (inventory.getCount(fallyTabs) == 0) {
                bank.withdraw(fallyTabs, 1);
            }
        } else {
            if (needNewGlory) {
                bank.withdraw(gap(gloryCharges), 1);
                sleep(random(1800, 2000));
                if (inventory.containsOneOf(gloryCharges)) {
                    needNewGlory = false;
                }
            }
        }
    }
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }
    private void walkPathAndInteractWithObject(RSTile[] path, RSObject object, int objectID, String interaction) {
        object = objects.getNearest(objectID);
        chargeSummoning();
        if (object != null) {
            camera.turnTo(object);
            if (object.isOnScreen() && calc.distanceTo(object) <= 4) {
                object.interact(interaction);
                sleep(random(2000, 4000));
            } else {
                walkPath(path);
            }
        } else {
            walkPath(path);
        }
    }
    private void managePrayers() {
        if (needToDrinkPray()) {
            drinkPrayerPotion();
        }

        if (useQuickPrayer) {
            if (!prayer.isQuickPrayerOn()) {
                prayer.setQuickPrayer(true);
                sleep(random(2000, 3000));
            }
        }

        if (!isProtected()) {
            if (prayer.getPrayerLeft() > 0) {
                setMagicPrayer();
            }
        }
    }
    private void manageTeleportaion() {
        if (needToTele()) {
            teleport();
        }
    }
    private void manageFood() {
        try {
            if (needToEat() && inventory.contains(food)) {
                inventory.getItem(food).interact("Eat");
                sleep(random(2000, 2500));
            }
        } catch (NullPointerException e) {
        }

    }
    private int cannonBallsAmount() {
        RSItem cb = inventory.getItem(cannonBalls);
        if (cb != null) {
            return cb.getStackSize();
        } else {
            return 0;
        }
    }
    private boolean cannonPlanted() {
        return inventory.getCount(cannonParts) == 0;
    }
    private boolean cannonNotFullyPlanted() {
        return inventory.getCount(cannonParts) > 0 && inventory.getCount(cannonParts) < 4;
    }
    private void recoverMessedUpCannon() {
        log("Recovering messed up cannon now!");
        RSObject cannon = objects.getNearest(messedUpCannonParts);
        if (cannon != null) {
            if (cannon.isOnScreen()) {
                cannon.interact("Pick-up");
                sleep(random(1000, 2000));
            } else {
                walking.walkTo(cannon.getLocation());
            }
        }
    }
    private boolean needToEat() {
        return getMyPlayer().getHPPercent() <= eatAt;
    }
    private void manageLooting() {
        try {
            lootItem = groundItems.getNearest(lootfilter);
        } catch (Exception e) {
            lootItem = null;
        }
        if (lootItem != null) {
            if (useScroll) {
                if (canDoScroll()) {
                    doWinterScroll();
                    sleep(random(900, 1200));
                }
            }
            if (needToStore()) {
                if (canStore()) {
                    if (!getMyPlayer().isMoving()) {
                        storeSummon();
                        sleep(random(1400, 1500));
                    }
                }
            }
            if (needToEatForLoot()) {
                eatForRoom();
            }
            if (!lootItem.isOnScreen()) {
                camera.turnTo(lootItem.getLocation());
            }
            if (lootItem.isOnScreen()) {
                int startItem = inventory.getCount(lootItem.getItem().getID());
                sleep(random(500, 600));
                try {
                    lootItem.interact("Take " + lootItem.getItem().getName());
                } catch (Exception e) {
                }
                sleep(1200, 1350);
                waitToMove();
                if (lootItem.getItem().getName().contains("visage")) {
                    visage++;
                }
                if (lootItem.getItem().getName().contains("Starved")) {
                    effigy++;
                }
                if (inventory.getCount(lootItem.getItem().getID()) > startItem) {
                    totalLoot += prices[getPriceIndex(lootItem)];
                }
            } else {
                try {
                    walking.walkTo(lootItem.getLocation());
                    sleep(random(1400, 1600));
                    waitToMove();
                } catch (NullPointerException e) {
                }
            }
        }
    }
    private boolean canStore() {
        return usingSummon && !summonFull && !needToSummon();
    }
    private boolean needToEatForLoot() {
        return inventory.isFull() && (summonFull || !usingSummon) && inventory.contains(food);
    }
    private void eatForRoom() {
        try {
            inventory.getItem(food).interact("Eat");
        } catch (NullPointerException e) {
        }
    }
    private void manageCannon() {
        RSGroundItem lootPriority = groundItems.getNearest(lootfilter);
        if (usingCannon && isInDungeon() && !doneWithCannon && lootPriority == null) {
            if (!cannonPlanted()) {
                log("We need to plant our cannon!");
                plantOurCannon();
                if (inventory.getCount(cannonParts) == 0) {
                    needToFill = true;
                    needToPickupCannon = false;
                    fillTimer.reset();
                    replaceTimer.reset();
                }
            } else if (needToRecoverCannon) {
                log("We need to recover our cannon, we've completely lost it!");
                recoverOurCannon();
            } else if (needToPickupCannon) {
                if (fillTimer.getRemaining() == 0) {
                    needToRecoverCannon = true;
                } else {
                    log("We need to reset our cannon timer!");
                    if (inventory.contains(food) && inventory.getCount() >= 24) {
                        inventory.getItem(food).interact("Eat");
                        sleep(random(700, 800));
                    } else {
                        doCannonAction("Pick-up");
                    }
                }
            } else if (needToFill) {
                if (replaceTimer.getRemaining() == 0 && inventory.getCountExcept(food) <= 24) {
                    needToPickupCannon = true;
                } else if (fillTimer.getRemaining() == 0) {
                    needToRecoverCannon = true;
                } else {
                    log("We need to fill our cannon!");
                    if (inventory.contains(cannonBalls)) {
                        doCannonAction("Fire");
                    }
                }
            } else if (cannonNotFullyPlanted()) {
                recoverMessedUpCannon();
            }
        }
    }
    private void waitToMove() {
        try {
            while (getMyPlayer().isMoving()) {
                sleep(random(10, 20));
            }
        } catch (NullPointerException e) {
        }
    }
    private void managePotting() {
        if (nAnti) {
            if (takingPotions[5]) {
                didPot(superantiPots);
            }
            if (takingPotions[0]) {
                didPot(antiPots);
            }
        }
        int[] potSkills = {Skills.RANGE, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENSE};
        if (takingPotions[1]) {
            doPots(potSkills[0], potionArrays[1]);
        } else if (takingPotions[6]) {
            doPots(potSkills[0], potionArrays[6]);
        }

        if (takingPotions[2]) {
            doPots(potSkills[1], potionArrays[2]);
        } else if (takingPotions[7]) {
            doPots(potSkills[1], potionArrays[7]);
        }

        if (takingPotions[3]) {
            doPots(potSkills[2], potionArrays[3]);
        } else if (takingPotions[8]) {
            doPots(potSkills[2], potionArrays[8]);
        }

        if (takingPotions[9]) {
            doPots(potSkills[3], potionArrays[9]);
        }
    }
    public void doPots(int skill, int[] pots) {
        int realLevel = skills.getRealLevel(skill);
        if (realLevel > 99) {
            realLevel = 99;
        }
        if (skills.getCurrentLevel(skill) - realLevel <= 9) {
            if (inventory.containsOneOf(pots)) {
                if (didPot(pots)) {
                    sleep(random(1000, 2000));
                }
            }
        }
    }
    private void plantOurCannon() {
        try {
            int location = getOurNewCannonLocation();
            cannonSpot = cannonSpots[location];
            if (combat.isAutoRetaliateEnabled()) {
                combat.setAutoRetaliate(false);
            }
            waitToMove();
            if (getMyPlayer().getLocation().getX() != cannonSpot.getX()
                    && getMyPlayer().getLocation().getY() != cannonSpot.getY()) {
                manageFood();
                managePrayers();
                manageTeleportaion();
                if (calc.distanceTo(cannonSpot) <= 5) {
                    walking.walkTileOnScreen(cannonSpot);
                    sleep(random(1100, 1200));
                } else {
                    walking.walkTileMM(cannonSpot);
                    sleep(random(1100, 1200));

                }
            } else if (inventory.getItem(cannonParts[0]).interact("Set-up")) {
                sleep(random(9000, 9500));
                walking.walkTileMM(cannonSpot);
                if (inventory.getCount(cannonParts) == 0) {
                    fullCannon = objects.getTopAt(cannonSpot);
                    sleep(random(700, 800));
                    if (fullCannon == null) {
                        fullCannon = objects.getNearest(fullCannonID);
                    }
                    cx = fullCannon.getLocation().getX();
                    cy = fullCannon.getLocation().getY();
                }
            }
            if (!combat.isAutoRetaliateEnabled() && cannonPlanted()) {
                combat.setAutoRetaliate(true);
            }
        } catch (NullPointerException e) {
        }
    }
    private void doCannonAction(String action) {
        if (fullCannon != null) {
            if (fullCannon.isOnScreen()) {
                fullCannon.interact(action);
                sleep(random(500, 700));
                waitToMove();
            } else {
                if (northFrosts.contains(fullCannon.getLocation())) {
                    walking.walkTileMM(fullCannon.getLocation());
                    sleep(random(900, 1200));
                } else {
                    log("Cannon is not in north frosts, we are regetting cannon");
                    objects.getAll();
                    fullCannon = objects.getTopAt(new RSTile(cx, cy));
                }
            }
        }
    }
    private void dropShit() {

        try {
            if (inventory.containsOneOf(drop)) {
                dropArray(drop);
            }
            if (inventory.containsOneOf(charmsToDropArray)) {
                dropArray(charmsToDropArray);
            }
        } catch (NullPointerException e) {
        }
    }
    private void dropArray(int[] array) {
        try {
            inventory.getItem(array).interact("Drop");
        } catch (NullPointerException e) {
        }
    }
    public void mouseClicked(MouseEvent e) {
        int x = (int) e.getPoint().getX();
        int y = (int) e.getPoint().getY();

        if (x <= 484 && x >= 474 && y <= 464 && y >= 454) {
            paint = !paint;
        }
    }
    private boolean areaFreeOfCannon(RSArea area) {
        try {
            RSTile[] alltiles = area.getTileArray();
            for (int i = 0; i < alltiles.length; i++) {
                RSObject[] allobject = objects.getAllAt(alltiles[i]);
                for (int j = 0; j < allobject.length; j++) {
                    if (allobject[j].getName().contains("Dwarf")) {
                        return false;
                    }
                }
            }
        } catch (NullPointerException e) {
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
    private RSArea getAreaFromTile(RSTile tile, int offset) {
        RSArea areaToCheck = new RSArea(new RSTile(tile.getX() - offset, tile.getY() - offset), new RSTile(tile.getX() + offset, tile.getY() + offset));
        return areaToCheck;
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    private void manageFighting() {
                try {
            lootItem = groundItems.getNearest(lootfilter);
        } catch (Exception e) {
            lootItem = null;
        }

                if(lootItem == null){
        try {
            dragon = npcs.getNearest(firstDragonFilter);
        } catch (ClassCastException e) {
            dragon = null;
        }
        if (dragon != null) {
            camera.turnTo(dragon);
            if (!getMyPlayer().isInCombat()) {
                dragon.interact("Attack");
                sleep(random(800, 1200));
            }
            if (dragon.getAnimation() == 13153) {
                if (canStore()) {
                    if (!getMyPlayer().isMoving()) {
                        storeSummon();
                        sleep(random(200, 400));
                    }
                }
            }
        } else if (!getMyPlayer().isInCombat()) {
            fightDragon(frostDragonFilter);
        }
        }
    }
    private void closePrayers() {
        try {
            if (useQuickPrayer) {
                if (prayer.isQuickPrayerOn()) {
                    prayer.setQuickPrayer(false);
                    sleep(random(1000, 1200));
                }
            }

            if (prayer.isCursing()) {
                if (prayer.isPrayerOn(Prayer.Curses.DEFLECT_MAGIC)) {
                    prayer.deactivateAll();
                }
            } else {
                if (prayer.isPrayerOn(Prayer.Normal.PROTECT_FROM_MAGIC)) {
                    prayer.deactivateAll();
                }
            }

        } catch (Exception e) {
        }
    }
    private void chargeSummoning() {
        try {
            if (usingSummon && summoning.getSummoningPoints() != skills.getRealLevel(Skills.SUMMONING)) {
                RSObject pillar = objects.getNearest(29953, 29954, 29952);
                if (pillar != null) {
                    if (pillar.isOnScreen()) {
                        pillar.interact("Renew");
                        sleep(random(1000, 1900));
                    }
                }
            }
        } catch (NullPointerException e) {
        } catch (IndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }
    }
    private boolean needToStore() {
        return usingSummon && !summonFull && inventory.getCount() >= 20;
    }
    private int getOurNewCannonLocation() {
        for (int i = 0; i < cannonSpots.length; i++) {
            if (areaFreeOfCannon(getAreaFromTile(cannonSpots[i], 3))) {
                return i;
            }
        }
        return random(0, 7);
    }
    private void fightDragon(Filter<RSNPC> filter) {
        dragon = npcs.getNearest(filter);
        if (dragon != null && !getMyPlayer().isMoving() && !getMyPlayer().isInCombat()) {
            if (dragon.isOnScreen()) {
                dragon.interact("Attack");
                sleep(random(600, 700));
            } else {
                camera.turnTo(dragon);
                dragon.interact("Attack");
            }
        }
    }
    private void openCorrectTab() {
        if (bank.getCurrentTab() != tab) {
            openBankTab(tab);
            sleep(random(800, 1000));
        }
    }
    private boolean areInDesiredBank() {
        return finalBankArea.contains(getMyPlayer().getLocation());
    }
    private boolean inTeleportLocation() {
        return finalTeleportArea.contains(getMyPlayer().getLocation());
    }
    private boolean haveTeleportationDevice() {
        if (bankIndex == 1) {
            return inventory.contains(fallyTabs) && inventory.contains(houseTabs);
        } else {
            return chargedGloryEquipped();
        }
    }
    private void doRingBusiness() {
        RSObject ring = objects.getNearest(12128);
        if (ring != null) {
            if (interfaces.get(735).isValid() == false) {
                if (ring.interact("Use")) {
                    waitForIface(735, random(1200, 1800));
                    sleep(240, 400);
                }
            }
            if (interfaces.get(735).isValid() == false) {
            } else {
                interfaces.get(735).getComponent(13).getComponent(0).doClick();
                sleep(1500, 2000);
                if (interfaces.get(734).getComponent(22).doClick()) {
                    sleep(random(2000, 3000));
                }
                sleep(random(700, 1000));
            }
        }
    }
    private boolean waitForIface(final int parent, final int timeout) {
        final long time = System.currentTimeMillis();
        while ((System.currentTimeMillis() - time) < timeout) {

            manageFood();
            managePrayers();
            manageTeleportaion();
            if (interfaces.get(parent).isValid()) {
                return true;
            }
            sleep(5, 15);
        }
        return interfaces.get(parent).isValid();
    }
    private boolean isInEdgeville() {
        return edgevilleCityArea.contains(getMyPlayer().getLocation());
    }
    private void gameChatModeColor() {
        Color gameChatModeColor = game.getColorAtPoint(109, 495);
        try {
            if (gameChatModeColor.getRGB() != -10464447) {
                game.setChatOption(Game.CHAT_OPTION_GAME, ChatMode.VIEW);
                sleep(random(1000, 2000));
            }
        } catch (Exception e) {
        }
    }
    private void doGloryRemoval() {
        if (bank.openEquipment()) {
            sleep(random(800, 1200));
            if (interfaces.get(667).getComponent(7).getComponent(2).doClick()) {
                sleep(random(800, 1200));
                if (interfaces.get(667).getComponent(48).doClick()) {
                    sleep(random(800, 1200));
                }
            }
        }
    }
    private boolean canDoScroll() {
        RSItem toStore = null;
        try {
            toStore = inventory.getItem(lootIDs[0], lootIDs[1], lootIDs[2], lootIDs[3], lootIDs[4], lootIDs[5], lootIDs[6]);
        } catch (Exception e) {
            return false;
        }
        return inventory.contains(12435) && usingSummon && useScroll && summoning.isFamiliarSummoned() && toStore != null;
    }
    private int getPriceIndex(RSGroundItem loot) {
        int id = loot.getItem().getID();
        for (int i = 0; i < lootIDs.length; i++) {
            if (id == lootIDs[i]) {
                return i;
            }
        }
        return 0;
    }
    private boolean needToTeleBeforeCannon() {
        return inventory.getCount(prayerPotions) == 0 && inventory.getCount(food) <= 1;
    }
    private void recoverOurCannon() {

        objects.getAll();
        fullCannon = objects.getTopAt(new RSTile(cx, cy));
        if (fullCannon == null) {
            cannonRecoverCounter++;
            if (cannonRecoverCounter <= 10) {
                RSObject[] allCannons = objects.getAll(cannonfilter);
                for (int i = 0; i < allCannons.length; i++) {
                    if (allCannons[i] != null) {
                        while (calc.distanceTo(allCannons[i]) > 3 && !allCannons[i].isOnScreen()) {
                            if (!isInDungeon()) {
                                break;
                            } else {
                                manageFood();
                                manageTeleportaion();
                                walking.walkTo(allCannons[i].getLocation());
                                waitToMove();
                            }
                        }
                        allCannons[i].interact("Pick-up");
                        sleep(random(1000, 2000));
                        waitToMove();
                        sleep(random(3000, 4000));
                        if (!cannonPlanted()) {
                            needToRecoverCannon = false;
                            fillTimer.reset();
                            cannonRecoverCounter = 0;
                            break;
                        }
                    }
                }
            } else {
                cannonRecoverCounter = 0;
                usingCannon = false;
                needToRecoverCannon = false;
                needCannonFromDwarf = true;
            }
        } else {
            needToRecoverCannon = false;
        }
    }
    private boolean waitForAnimation() {
        int timeout = 3000;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeout) {
            if (getMyPlayer().getAnimation() == 829) {
                return true;
            }
        }
        return false;
    }
    public class FDKGUI extends javax.swing.JFrame {
        public FDKGUI() {
            initComponents();
            this.setLocation(450, 250);
            this.setResizable(true);
        }
        private void initComponents() {

            buttonGroup1 = new javax.swing.ButtonGroup();
            jLabel5 = new javax.swing.JLabel();
            jSeparator1 = new javax.swing.JSeparator();
            jLabel1 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jLabel2 = new javax.swing.JLabel();
            jTextField2 = new javax.swing.JTextField();
            jLabel6 = new javax.swing.JLabel();
            jSpinner1 = new javax.swing.JSpinner();
            jLabel7 = new javax.swing.JLabel();
            jSeparator2 = new javax.swing.JSeparator();
            jCheckBox2 = new javax.swing.JCheckBox();
            jCheckBox3 = new javax.swing.JCheckBox();
            jCheckBox4 = new javax.swing.JCheckBox();
            jCheckBox5 = new javax.swing.JCheckBox();
            jCheckBox1 = new javax.swing.JCheckBox();
            jButton1 = new javax.swing.JButton();
            jLabel8 = new javax.swing.JLabel();
            jSeparator3 = new javax.swing.JSeparator();
            jCheckBox7 = new javax.swing.JCheckBox();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel9 = new javax.swing.JLabel();
            jLabel10 = new javax.swing.JLabel();
            jSeparator5 = new javax.swing.JSeparator();
            jLabel11 = new javax.swing.JLabel();
            jLabel12 = new javax.swing.JLabel();
            jCheckBox6 = new javax.swing.JCheckBox();
            jSpinner2 = new javax.swing.JSpinner();
            jLabel14 = new javax.swing.JLabel();
            jLabel15 = new javax.swing.JLabel();
            jSpinner3 = new javax.swing.JSpinner();
            jLabel16 = new javax.swing.JLabel();
            jSpinner4 = new javax.swing.JSpinner();
            jLabel17 = new javax.swing.JLabel();
            jSpinner5 = new javax.swing.JSpinner();
            jLabel18 = new javax.swing.JLabel();
            jSpinner6 = new javax.swing.JSpinner();
            jCheckBox8 = new javax.swing.JCheckBox();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton3 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jRadioButton4 = new javax.swing.JRadioButton();
            jRadioButton5 = new javax.swing.JRadioButton();
            jRadioButton6 = new javax.swing.JRadioButton();
            jCheckBox11 = new javax.swing.JCheckBox();
            jLabel13 = new javax.swing.JLabel();
            jSeparator6 = new javax.swing.JSeparator();
            jLabel19 = new javax.swing.JLabel();
            jSpinner7 = new javax.swing.JSpinner();
            jLabel20 = new javax.swing.JLabel();
            jSpinner8 = new javax.swing.JSpinner();
            jLabel21 = new javax.swing.JLabel();
            jSpinner11 = new javax.swing.JSpinner();
            jCheckBox9 = new javax.swing.JCheckBox();
            jSpinner12 = new javax.swing.JSpinner();
            jLabel24 = new javax.swing.JLabel();
            jSpinner13 = new javax.swing.JSpinner();
            jLabel25 = new javax.swing.JLabel();
            jCheckBox10 = new javax.swing.JCheckBox();
            jCheckBox12 = new javax.swing.JCheckBox();
            jCheckBox13 = new javax.swing.JCheckBox();
            jCheckBox14 = new javax.swing.JCheckBox();
            jSeparator7 = new javax.swing.JSeparator();
            jLabel22 = new javax.swing.JLabel();
            jSeparator8 = new javax.swing.JSeparator();
            jRadioButton7 = new javax.swing.JRadioButton();
            jLabel3 = new javax.swing.JLabel();
            jTextField3 = new javax.swing.JTextField();
            jCheckBox15 = new javax.swing.JCheckBox();
            jTextField4 = new javax.swing.JTextField();
            jLabel4 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Ownageful's Frost Dragon Killer");
            setAlwaysOnTop(true);
            setBackground(new java.awt.Color(255, 255, 255));
            setFocusable(false);

            jLabel5.setForeground(new java.awt.Color(0, 153, 51));
            jLabel5.setText("Food:");

            jLabel1.setText("Food ID:");

            jLabel2.setText("Withdraw:");

            jLabel6.setText("Eat around:");

            jSpinner1.setModel(new javax.swing.SpinnerNumberModel(70, 20, 90, 1));

            jLabel7.setForeground(new java.awt.Color(204, 0, 102));
            jLabel7.setText("Pots:");

            jCheckBox2.setText("Drink Antifire");

            jCheckBox3.setText("Drink Super Attack");

            jCheckBox4.setText("Drink Range Pots");

            jCheckBox5.setText("Drink Super Strength");

            jCheckBox1.setText("Loot Charms?");

            jButton1.setText("Start");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jLabel8.setForeground(new java.awt.Color(0, 51, 255));
            jLabel8.setText("Summoning: ");

            jCheckBox7.setText("Beast of Burden:");

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bull Ant", "TerrorBird", "War Tortoise", "Pack Yak"}));

            jLabel9.setForeground(new java.awt.Color(255, 0, 0));
            jLabel9.setText("Looting:");

            jLabel10.setForeground(new java.awt.Color(255, 0, 255));
            jLabel10.setText("Cannon:");

            jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18));
            jLabel11.setForeground(new java.awt.Color(0, 102, 255));
            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Ownageful's Frost Dragon Killer Pro");

            jLabel12.setText("%");

            jCheckBox6.setText("Drink Prayer Pots");

            jSpinner2.setModel(new javax.swing.SpinnerNumberModel(2, 1, 4, 1));

            jLabel14.setText("potions per trip");

            jLabel15.setText("potions per trip");

            jSpinner3.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel16.setText("potions per trip");

            jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel17.setText("potions per trip");

            jSpinner5.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel18.setText("potions per trip");

            jSpinner6.setModel(new javax.swing.SpinnerNumberModel(4, 1, 16, 1));

            jCheckBox8.setText("Use Cannon (start with all peices in inventory)");

            jRadioButton1.setText("Green");

            jRadioButton3.setText("Gold");

            jRadioButton2.setText("Blue");

            jRadioButton4.setText("Crimson");

            buttonGroup1.add(jRadioButton5);
            jRadioButton5.setSelected(true);
            jRadioButton5.setText("Draynor bank method (must have charged glories)");

            buttonGroup1.add(jRadioButton6);
            jRadioButton6.setText("Falador to House method (must have fally tabs)");

            jCheckBox11.setText("Use quick prayer (if yes, please set magic prayer as one of them)");

            jLabel13.setForeground(new java.awt.Color(255, 0, 255));
            jLabel13.setText("Prayer");

            jLabel19.setText("potions per trip");

            jSpinner7.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel20.setText("potions per trip");

            jSpinner8.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel21.setText("potions per trip");

            jSpinner11.setModel(new javax.swing.SpinnerNumberModel(1, 1, 8, 1));

            jCheckBox9.setText("Drink Defence Potions");

            jSpinner12.setModel(new javax.swing.SpinnerNumberModel(2, 1, 4, 1));

            jLabel24.setText("potions per trip");

            jSpinner13.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

            jLabel25.setText("potions per trip");

            jCheckBox10.setText("Drink Extreme Super Strength");

            jCheckBox12.setText("Drink Extreme Super Attack");

            jCheckBox13.setText("Drink Extreme Range Pots");

            jCheckBox14.setText("Drink Super Antifire");

            jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

            jLabel22.setForeground(new java.awt.Color(255, 0, 255));
            jLabel22.setText("Banking Method:");

            jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

            buttonGroup1.add(jRadioButton7);
            jRadioButton7.setText("Edgeville to Fairy Ring method (fairy tale part two req'd)");

            jLabel3.setText("Amount of balls to take:");

            jTextField3.setText("600");

            jCheckBox15.setText("Use Winter Scroll (Yak only)");

            jTextField4.setText("200");

            jLabel4.setText("Take how many scrolls:");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 550, Short.MAX_VALUE)).addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE).addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox5).addComponent(jCheckBox3).addComponent(jCheckBox4).addComponent(jCheckBox2).addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(17, 17, 17).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel16)).addGroup(layout.createSequentialGroup().addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel15)).addGroup(layout.createSequentialGroup().addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel14)))).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel18)).addGroup(layout.createSequentialGroup().addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel17)))))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel8))).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox10).addComponent(jCheckBox12).addComponent(jCheckBox13).addComponent(jCheckBox14).addComponent(jCheckBox9)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(17, 17, 17).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel20)).addGroup(layout.createSequentialGroup().addComponent(jSpinner13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel25)).addGroup(layout.createSequentialGroup().addComponent(jSpinner12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel24)))).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSpinner11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel21)).addGroup(layout.createSequentialGroup().addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel19)))))))).addGroup(layout.createSequentialGroup().addGap(91, 91, 91).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)).addGroup(layout.createSequentialGroup().addGap(138, 138, 138).addComponent(jLabel11)).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jCheckBox8)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel13)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox11).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)))).addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jCheckBox7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jCheckBox15).addGroup(layout.createSequentialGroup().addGap(9, 9, 9).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(24, 24, 24).addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(8, 8, 8).addComponent(jCheckBox1).addGap(79, 79, 79).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton1)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton3).addComponent(jRadioButton4))).addGroup(layout.createSequentialGroup().addComponent(jLabel10).addGap(229, 229, 229).addComponent(jLabel22)).addGroup(layout.createSequentialGroup().addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(72, 72, 72).addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton7).addComponent(jRadioButton6).addComponent(jRadioButton5)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE))).addContainerGap()));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel10).addGroup(layout.createSequentialGroup().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addGap(3, 3, 3).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addGap(13, 13, 13).addComponent(jLabel7).addGap(1, 1, 1).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(29, 29, 29).addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(35, 35, 35).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14).addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16).addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGap(4, 4, 4).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(29, 29, 29).addComponent(jCheckBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(35, 35, 35).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel24).addComponent(jCheckBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel25)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel20).addComponent(jCheckBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGap(4, 4, 4).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(jSpinner11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jLabel9)).addGap(1, 1, 1).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox1).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel22).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox7).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(9, 9, 9).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jCheckBox8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14, 14, 14).addComponent(jLabel13)).addGroup(layout.createSequentialGroup().addComponent(jRadioButton5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton7))).addGap(5, 5, 5).addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox11))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1).addContainerGap()));
            loadProperties();
            pack();
        }// </editor-fold>
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            boolean error = false;
            loots.clear();
            loots.add(new Item("Frost dragon bones", 18832));
            loots.add(new Item("Dragon spear", 1249));
            loots.add(new Item("Rune longsword", 1303));
            loots.add(new Item("Rune spear", 1247));
            loots.add(new Item("Draconic visage", 11286));
            loots.add(new Item("Loop half of a key", 985));
            loots.add(new Item("Tooth half of a key", 987));
            loots.add(new Item("Starved ancient effigy", 18778));
            if (jTextField1.getText().isEmpty()) {
                usingFood = false;
            } else {
                try {
                    food = Integer.parseInt(jTextField1.getText());
                    eatAt = (Integer) jSpinner1.getValue();
                    foodAmount = Integer.parseInt(jTextField2.getText());
                    usingFood = true;
                } catch (NumberFormatException numberFormatException) {
                    WindowUtil.showDialog("Invalid Food format, please enter proper ID numbers");
                    error = true;
                }
            }

            JCheckBox[] boxes = {jCheckBox2, jCheckBox4, jCheckBox3, jCheckBox5, jCheckBox6, jCheckBox14, jCheckBox13, jCheckBox12, jCheckBox10, jCheckBox9};
            JSpinner[] spinners = {jSpinner2, jSpinner3, jSpinner4, jSpinner5, jSpinner6, jSpinner12, jSpinner13, jSpinner8, jSpinner7, jSpinner11};
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i].isSelected()) {
                    takingPotions[i] = true;
                    potionAmounts[i] = (Integer) spinners[i].getValue();
                }
            }
            if (jRadioButton1.isSelected() && jCheckBox1.isSelected()) {
                loots.add(new Item("Green charm", 12159));
            } else {
                charmsToDrop.add(12159);
            }
            if (jRadioButton2.isSelected() && jCheckBox1.isSelected()) {
                loots.add(new Item("Blue charm", 12163));
            } else {
                charmsToDrop.add(12163);
            }
            if (jRadioButton3.isSelected() && jCheckBox1.isSelected()) {
                loots.add(new Item("Gold charm", 12158));
            } else {
                charmsToDrop.add(12158);
            }
            if (jRadioButton4.isSelected() && jCheckBox1.isSelected()) {
                loots.add(new Item("Crimson charm", 12160));
            } else {
                charmsToDrop.add(12160);
            }

            if (jCheckBox7.isSelected()) {
                usingSummon = true;
                finalPouch = bobPouchIDS[jComboBox1.getSelectedIndex()];
            }

            if (jCheckBox8.isSelected()) {
                usingCannon = true;
                try {
                    cannonBallsToTake = Integer.parseInt(jTextField3.getText());
                } catch (NumberFormatException numberFormatException) {
                    cannonBallsToTake = 600;
                }
            }

            if (jCheckBox11.isSelected()) {
                useQuickPrayer = true;
            }
            if (jCheckBox15.isSelected()) {
                if (jComboBox1.getSelectedIndex() == 3) {
                    useScroll = true;
                    scrollsToTake = Integer.parseInt(jTextField4.getText());
                }
            }

            if (jRadioButton5.isSelected()) {
                finalBankArea = draynorBank;
                finalTeleportArea = draynorTeleportArea;
                finalBankCentralTile = draynorBankCentralTile;
                finalTeleportAnimation = 9603;
                finalBanker = draynorBanker;
                bankIndex = 0;
                finalPathToTrap = draynorBankToTrap;
                gloryAction = "Draynor";
                outsideBankTile = new RSTile(3087, 3249);
            } else if (jRadioButton6.isSelected()) {
                finalBankArea = faladorBank;
                finalTeleportArea = faladorTeleportArea;
                finalBankCentralTile = faladorBankCentralTile;
                finalTeleportAnimation = 9597;
                finalBanker = fallyBanker;
                finalPathToTrap = houseToTrap;
                outsideBankTile = new RSTile(2946, 3375);
                bankIndex = 1;
            } else if (jRadioButton7.isSelected()) {
                finalBankArea = edgevilleBank;
                finalTeleportArea = edgevilleTeleportArea;
                finalBankCentralTile = edgevilleBankCentralTile;
                finalPathToTrap = fairyRingToTrap;
                finalTeleportAnimation = 9603;
                finalBanker = edgevilleBanker;
                gloryAction = "Edgeville";
                bankIndex = 2;
                outsideBankTile = new RSTile(3101, 3500);
            }

            if (!error) {
                createLootArrays();
                this.dispose();
            }
        }
        private javax.swing.ButtonGroup buttonGroup1;
        private javax.swing.JButton jButton1;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox10;
        private javax.swing.JCheckBox jCheckBox11;
        private javax.swing.JCheckBox jCheckBox12;
        private javax.swing.JCheckBox jCheckBox13;
        private javax.swing.JCheckBox jCheckBox14;
        private javax.swing.JCheckBox jCheckBox15;
        private javax.swing.JCheckBox jCheckBox2;
        private javax.swing.JCheckBox jCheckBox3;
        private javax.swing.JCheckBox jCheckBox4;
        private javax.swing.JCheckBox jCheckBox5;
        private javax.swing.JCheckBox jCheckBox6;
        private javax.swing.JCheckBox jCheckBox7;
        private javax.swing.JCheckBox jCheckBox8;
        private javax.swing.JCheckBox jCheckBox9;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel13;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel19;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel20;
        private javax.swing.JLabel jLabel21;
        private javax.swing.JLabel jLabel22;
        private javax.swing.JLabel jLabel24;
        private javax.swing.JLabel jLabel25;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JRadioButton jRadioButton1;
        private javax.swing.JRadioButton jRadioButton2;
        private javax.swing.JRadioButton jRadioButton3;
        private javax.swing.JRadioButton jRadioButton4;
        private javax.swing.JRadioButton jRadioButton5;
        private javax.swing.JRadioButton jRadioButton6;
        private javax.swing.JRadioButton jRadioButton7;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JSeparator jSeparator2;
        private javax.swing.JSeparator jSeparator3;
        private javax.swing.JSeparator jSeparator5;
        private javax.swing.JSeparator jSeparator6;
        private javax.swing.JSeparator jSeparator7;
        private javax.swing.JSeparator jSeparator8;
        private javax.swing.JSpinner jSpinner1;
        private javax.swing.JSpinner jSpinner11;
        private javax.swing.JSpinner jSpinner12;
        private javax.swing.JSpinner jSpinner13;
        private javax.swing.JSpinner jSpinner2;
        private javax.swing.JSpinner jSpinner3;
        private javax.swing.JSpinner jSpinner4;
        private javax.swing.JSpinner jSpinner5;
        private javax.swing.JSpinner jSpinner6;
        private javax.swing.JSpinner jSpinner7;
        private javax.swing.JSpinner jSpinner8;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTextField jTextField3;
        private javax.swing.JTextField jTextField4;
        private void createLootArrays() {
            lootIDs = new int[loots.size()];
            lootNames = new String[loots.size()];
            prices = new int[loots.size()];
            for (int i = 0; i < lootIDs.length; i++) {
                lootIDs[i] = loots.get(i).getID();
                lootNames[i] = loots.get(i).getName();
            }
            charmsToDropArray = new int[charmsToDrop.size()];
            for (int j = 0; j < charmsToDrop.size(); j++) {
                charmsToDropArray[j] = charmsToDrop.get(j);
            }
        }
        private void loadProperties() {
            String cache = Configuration.Paths.getScriptCacheDirectory();
            File f = new File(cache + File.separator + "frostSettings.prop");
            if (f.exists()) {
                log("Loading properties from file, it exists!");
                props = new Properties();
                try {
                    FileInputStream fis = new FileInputStream(f);
                    props.load(fis);
                    log("1");
                    jTextField1.setText(props.getProperty("foodid"));
                    log("2");
                    jTextField2.setText(props.getProperty("foodamount"));
                    log("3");
                    jSpinner1.setValue(Integer.parseInt(props.getProperty("eatat")));
                    log("4");
                    JCheckBox[] boxes = {jCheckBox2, jCheckBox4, jCheckBox3, jCheckBox5, jCheckBox6, jCheckBox14, jCheckBox13, jCheckBox12, jCheckBox10, jCheckBox9};
                    JSpinner[] spinners = {jSpinner2, jSpinner3, jSpinner4, jSpinner5, jSpinner6, jSpinner12, jSpinner13, jSpinner8, jSpinner7, jSpinner11};
                    String[] selections = {"antifire", "rangepots", "superattack", "superstrength", "prayerpots", "superanti", "extremerange", "extremeattack", "extremestrength", "extremedef"};
                    String[] ammounts = {"antifireamt", "rangepotsamt", "superattackamt", "superstrengthamt", "prayerpotsamt", "superantiamt", "extremerangeamt", "extremeattackamt", "extremestrengthamt", "extremedefamt"};
                    log("5");
                    for (int i = 0; i < boxes.length; i++) {
                        if (props.getProperty(selections[i]).equals("1")) {
                            boxes[i].setSelected(true);
                        }
                        spinners[i].setValue(Integer.parseInt(props.getProperty(ammounts[i])));
                    }

                    log("6");
                    if (props.getProperty("summoning").equals("1")) {
                        jCheckBox7.setSelected(true);
                    }

                    if (props.getProperty("usingcannon").equals("1")) {
                        jCheckBox8.setSelected(true);
                    }
                    jTextField3.setText(props.getProperty("cannonballs"));

                    log("7");
                    jComboBox1.setSelectedIndex(Integer.parseInt(props.getProperty("boxindex")));

                    log("8");
                    if (props.getProperty("lootcharms").equals("1")) {
                        jCheckBox1.setSelected(true);
                    }

                    log("9");
                    JRadioButton[] lootObjects = {jRadioButton1, jRadioButton2, jRadioButton3, jRadioButton4};
                    String[] lootProps = {"green", "blue", "gold", "crimson"};
                    for (int i = 0; i < lootProps.length; i++) {
                        if (props.getProperty(lootProps[i]).equals("1")) {
                            lootObjects[i].setSelected(true);
                        }
                    }

                    log("10");
                    JRadioButton[] paths = {jRadioButton5, jRadioButton6, jRadioButton7};
                    paths[Integer.parseInt(props.getProperty("bankingmethod"))].setSelected(true);
                    if (props.getProperty("usequickprayer").equals("1")) {
                        jCheckBox11.setSelected(true);
                    }
                    //jCheckBox15
                    if (props.getProperty("usewinter").equals("1")) {
                        jCheckBox15.setSelected(true);
                    }
                    jTextField4.setText(props.getProperty("winteramt"));
                    //        jTextField4
                    fis.close();
                } catch (IOException iOException) {
                    log("IO error now!");
                }
            }
        }
        private void setProperties() {
            props = new Properties();
            props.setProperty("foodid", jTextField1.getText());
            props.setProperty("foodamount", jTextField2.getText());
            props.setProperty("eatat", jSpinner1.getValue() + "");
            JCheckBox[] boxes = {jCheckBox2, jCheckBox4, jCheckBox3, jCheckBox5, jCheckBox6, jCheckBox14, jCheckBox13, jCheckBox12, jCheckBox10, jCheckBox9};
            JSpinner[] spinners = {jSpinner2, jSpinner3, jSpinner4, jSpinner5, jSpinner6, jSpinner12, jSpinner13, jSpinner8, jSpinner7, jSpinner11};
            String[] selections = {"antifire", "rangepots", "superattack", "superstrength", "prayerpots", "superanti", "extremerange", "extremeattack", "extremestrength", "extremedef"};
            String[] ammounts = {"antifireamt", "rangepotsamt", "superattackamt", "superstrengthamt", "prayerpotsamt", "superantiamt", "extremerangeamt", "extremeattackamt", "extremestrengthamt", "extremedefamt"};
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i].isSelected()) {
                    props.setProperty(selections[i], "1");
                } else {
                    props.setProperty(selections[i], "0");
                }
                props.setProperty(ammounts[i], spinners[i].getValue() + "");
            }
            if (jCheckBox7.isSelected()) {
                props.setProperty("summoning", "1");
            } else {
                props.setProperty("summoning", "0");
            }
            props.setProperty("boxindex", jComboBox1.getSelectedIndex() + "");
            if (jCheckBox1.isSelected()) {
                props.setProperty("lootcharms", "1");
            } else {
                props.setProperty("lootcharms", "0");
            }
            JRadioButton[] lootObjects = {jRadioButton1, jRadioButton2, jRadioButton3, jRadioButton4};
            String[] lootProps = {"green", "blue", "gold", "crimson"};
            for (int i = 0; i < lootProps.length; i++) {
                if (lootObjects[i].isSelected()) {
                    props.setProperty(lootProps[i], "1");
                } else {
                    props.setProperty(lootProps[i], "0");
                }
            }
            props.setProperty("cannonballs", jTextField3.getText());

            if (jCheckBox8.isSelected()) {
                props.setProperty("usingcannon", "1");
            } else {
                props.setProperty("usingcannon", "0");
            }
            JRadioButton[] paths = {jRadioButton5, jRadioButton6, jRadioButton7};
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].isSelected()) {
                    props.setProperty("bankingmethod", i + "");
                    break;
                }
            }
            if (jCheckBox11.isSelected()) {
                props.setProperty("usequickprayer", "1");
            } else {
                props.setProperty("usequickprayer", "0");
            }
            props.setProperty("winteramt", jTextField4.getText());
            if (jCheckBox15.isSelected()) {
                props.setProperty("usewinter", "1");
            } else {
                props.setProperty("usewinter", "0");
            }

            String cache = Configuration.Paths.getScriptCacheDirectory();
            File f = new File(cache + File.separator + "frostSettings.prop");
            try {
                FileOutputStream out = new FileOutputStream(f.getPath());
                props.store(out, status);
            } catch (IOException iOException) {
            }
        }
    }
    public class Item {
        String name;
        int id;
        public Item(String name, int id) {
            this.name = name;
            this.id = id;
        }
        public int getID() {
            return this.id;
        }
        public String getName() {
            return this.name;
        }
    }
    public class TimeListener {
        long startTime = 0;
        long pausedTime = 0;
        public TimeListener() {
            startTime = System.currentTimeMillis();
        }
        public long getMillis() {
            return System.currentTimeMillis() - startTime - pausedTime;
        }
        public long getSeconds() {
            return this.getMillis() / 1000;
        }
        public long getMinutes() {
            return this.getSeconds() / 60;
        }
        public long getHours() {
            return this.getMinutes() / 60;
        }
        public String getRuntimeString() {
            final long HoursRan = this.getHours();
            long MinutesRan = this.getMinutes();
            long SecondsRan = this.getSeconds();
            MinutesRan = MinutesRan % 60;
            SecondsRan = SecondsRan % 60;
            return HoursRan + ":" + MinutesRan + ":" + SecondsRan;
        }
        public long calcPerHour(final long i) {
            return calcPerHour((double) i);
        }
        public long calcPerHour(final double i) {
            final double elapsed_millis = this.getMillis();
            return (long) ((i / elapsed_millis) * 3600000);
        }
    }
}
