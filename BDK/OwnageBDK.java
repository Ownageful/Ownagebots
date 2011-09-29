import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
@ScriptManifest(authors = {"Ownageful, Aut0r"}, name = "Blue Dragon Killer", version = 1.4, description = "<html><body style='font-family: Arial; margin: 10px;'><span style='color: #0000A0; font-weight: bold;'>Blue Dragon Killer</span>&nbsp;<strong>Version:&nbsp;1.4</strong><br />")
public class OwnageBDK extends Script implements PaintListener, MessageListener, MouseListener {
    public long startTime = System.currentTimeMillis();
    public int agility1 = 11844, finalPouch = -1, eatAt = -1, fallytab = 8009;
    double version = 1.4;
    String[] names = {"Dragon bones", "Tooth", "Rune ful", "Uncut dragonstone", "Dragon spear", "Rune spear", "Loop", "Shield left", "Starved"};
    private int[] charms;
    ArrayList<Integer> charmsAL = new ArrayList<Integer>();
    String[] hideNames = {"Blue drag"};
    private int[] prices = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] hidePrices = {0};
    private boolean shortcut = false, summonFull = false;
    private int[] strengthPots = {2440, 157, 159, 161};
    private int[] attackPots = {2436, 145, 147, 149};
    private int[] antiPots = {2452, 2454, 2456, 2458};
    private int[] rangedPots = {2444, 169, 171, 173};
    private int[] overloads = {15335, 15334, 15333, 15332};
    private int[] combatPots = {9739, 9741, 9743, 9745};
    private int[] bobPouchIDS = {12087, 12007, 12031, 12093};
    private int[] summonPots = {12146, 12144, 12142, 12140};
    private int[] loots = {536, 985, 1163, 1631, 1249, 1247, 987, 2366, 18778};
    private int[] hideLoots = {1751};
    private int[] drop = {229, 1365, 995, 1355, 1069, 555, 1179, 205, 207, 209, 211, 213, 215, 217, 2485, 3051, 3049, 563, 561, 9142, 203, 449};
    private int[] bankBoundry = {2943, 3368, 2949, 3373};
    public int tab, ptab = 1, dusty = 1590;
    private boolean dPots = false, usingFood = false, takePots = false, nStop = false, rPots = false, oPots = false, cPots = false, dAnti = false, lHides = false, lCharms = false, useSummon = false, dungeon = false, tabTaken = false, nBank = false;
    private int food, withdraw, totalLoot, lph, bankRun = 0;
    private RSTile center = new RSTile(2902, 9803);
    private RSGroundItem charmz, lootzd, lootzh, lootz;
    private Timer tm = new Timer(0);
    RSTile[] toBank = {new RSTile(2952, 3379), new RSTile(2946, 3368)};
    private RSNPC drag;
    private BDKGUI gui;
    private boolean nanti;
    private String st, stat;
    private RSTile[] centerDung = {new RSTile(1002, 4509), new RSTile(987, 4502)};
    private RSTile[] agilitysctotravdung = {new RSTile(2922, 3364),
        new RSTile(2912, 3373), new RSTile(2898, 3377),
        new RSTile(2890, 3391), new RSTile(2884, 3395)};
    RSTile summonRecharge = new RSTile(2890, 9788);
    private final RSTile[] LADDERS_TO_GATE_ENTRANCE = {new RSTile(2884, 9800),
        new RSTile(2884, 9802), new RSTile(2884, 9804),
        new RSTile(2884, 9806), new RSTile(2884, 9808),
        new RSTile(2884, 9810), new RSTile(2884, 9812),
        new RSTile(2884, 9814), new RSTile(2884, 9817),
        new RSTile(2884, 9820), new RSTile(2884, 9824),
        new RSTile(2884, 9826), new RSTile(2885, 9829),
        new RSTile(2885, 9832), new RSTile(2885, 9836),
        new RSTile(2885, 9839), new RSTile(2883, 9842),
        new RSTile(2885, 9844), new RSTile(2888, 9845),
        new RSTile(2890, 9847), new RSTile(2893, 9849),
        new RSTile(2897, 9849), new RSTile(2901, 9849),
        new RSTile(2905, 9849), new RSTile(2909, 9849),
        new RSTile(2913, 9849), new RSTile(2917, 9849),
        new RSTile(2921, 9848), new RSTile(2924, 9846),
        new RSTile(2926, 9843), new RSTile(2929, 9840),
        new RSTile(2933, 9837), new RSTile(2935, 9833),
        new RSTile(2937, 9830), new RSTile(2937, 9827),
        new RSTile(2938, 9823), new RSTile(2938, 9820),
        new RSTile(2938, 9816), new RSTile(2938, 9812),
        new RSTile(2940, 9809), new RSTile(2941, 9806),
        new RSTile(2942, 9802), new RSTile(2944, 9799),
        new RSTile(2945, 9796), new RSTile(2949, 9795),
        new RSTile(2951, 9791), new RSTile(2951, 9788),
        new RSTile(2951, 9785), new RSTile(2951, 9782),
        new RSTile(2951, 9779), new RSTile(2951, 9776),
        new RSTile(2949, 9774), new RSTile(2946, 9774),
        new RSTile(2943, 9776), new RSTile(2940, 9778),
        new RSTile(2937, 9778), new RSTile(2935, 9775),
        new RSTile(2935, 9772), new RSTile(2934, 9768),
        new RSTile(2934, 9765), new RSTile(2934, 9762),
        new RSTile(2935, 9759), new RSTile(2934, 9757),
        new RSTile(2931, 9756), new RSTile(2928, 9756),
        new RSTile(2925, 9756), new RSTile(2923, 9759),
        new RSTile(2924, 9762), new RSTile(2924, 9765),
        new RSTile(2925, 9768), new RSTile(2927, 9771),
        new RSTile(2930, 9774), new RSTile(2930, 9777),
        new RSTile(2931, 9781), new RSTile(2937, 9790),
        new RSTile(2930, 9802), new RSTile(2924, 9803)};
    private transient final Filter<RSNPC> outsideFilter = new Filter<RSNPC>() {
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equals("Blue dragon")
                        && !npc.isInCombat()
                        && npc.getInteracting() != getMyPlayer()
                        && npc.getAnimation() == -1 && !npc.isInteractingWithLocalPlayer());
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    private transient final Filter<RSNPC> dungeonFilter = new Filter<RSNPC>() {
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equals("Blue dragon")
                        && (npc.getInteracting() == null
                        || npc.getInteracting().equals(getMyPlayer())
                        || getMyPlayer().getInteracting().equals(npc))
                        && npc.getAnimation() != 12250);
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    @Override
    public boolean onStart() {
        if (game.isLoggedIn()) {
            createAndWaitforGUI();
            if (nStop) {
                return false;
            }
            combat.setAutoRetaliate(true);
            mouse.setSpeed(random(4, 5));
            tm.reset();
            return true;
        }
        log.severe("Please log in before startnig the script.");
        return false;
    }
    private void createAndWaitforGUI() {
        gui = new BDKGUI();
        gui.setVisible(true);
        threadStart();
        while (gui.isVisible()) {
            sleep(20);
        }
    }
    @Override
    public int loop() {
        if (nStop) {
            stopScript();
        }
        minorChecks();
        if (nBank) {
            if (isInDungeon() || isInTaverly()) {
                inventory.getItem(fallytab).doAction("Break");
                sleep(random(9000, 9500));
            }
            stat = "Walking to Bank";
            try {
                walking.walkTileMM(walking.getPath(toBank[1]).getNext());
                sleep(random(700, 1200));
            } catch (NullPointerException e) {
                return 100;
            }
            if (isInArea(bankBoundry)) {
                bankRun++;
                nBank = false;
            }
        } else if (isInArea(bankBoundry)) {
            stat = "Banking";
            if (inventory.contains(fallytab) && (inventory.contains(food) || !usingFood)
                    && !inventory.contains(1751) && !inventory.contains(536) && (!useSummon || (useSummon && inventory.contains(finalPouch)))) {
                stat = "Walking to wall";
                nBank = false;
                walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
                sleep(random(4000, 5000));
            } else {
                RSObject b = objects.getNearest(11758);
                if (b != null) {
                    camera.turnTo(b);
                    if (!b.isOnScreen()) {
                        walking.walkTileMM(b.getLocation());
                    } else {
                        bank.open();
                        sleep(random(2000, 2200));
                        if (bank.isOpen()) {
                            if (!tabTaken) {
                                sleep(random(3000, 3500));
                                tab = bank.getCurrentTab();
                                tabTaken = true;
                            } else {
                                if (bank.getCurrentTab() != tab) {
                                    openBankTab(tab);
                                    sleep(random(2000, 3000));
                                }
                            }
                            try {
                                if (bank.depositAllExcept(fallytab, dusty, finalPouch, summonPots[0], summonPots[1], summonPots[2], summonPots[3])) {
                                    if (summoning.isFamiliarSummoned()) {
                                        bank.depositAllFamiliar();
                                    }
                                    summonFull = false;
                                    sleep(random(1000, 2000));
                                    if (usingFood && bank.isOpen()) {
                                        while (!inventory.contains(food)) {

                                            if (!bank.isOpen()) {
                                                bank.open();
                                                sleep(random(1000, 2000));
                                            }

                                            if (bank.getCurrentTab() != tab) {
                                                openBankTab(tab);
                                                sleep(random(2000, 3000));
                                            }


                                            if (bank.getItem(food) == null) {
                                                log("Out of food, stopping script");
                                                this.stopScript();
                                                super.stopScript();
                                                break;
                                            }

                                            if (oPots) {
                                                bank.withdraw(food, withdraw + 5);
                                                sleep(random(800, 1000));
                                            } else {
                                                bank.withdraw(food, withdraw);
                                                sleep(random(800, 1000));
                                            }
                                        }
                                    }
                                    if (dPots) {
                                        bank.withdraw(gap(strengthPots), 1);
                                        sleep(random(800, 1000));

                                        bank.withdraw(gap(attackPots), 1);
                                        sleep(random(800, 1000));
                                    }
                                    if (rPots) {
                                        bank.withdraw(gap(rangedPots), 1);
                                        sleep(random(800, 1000));
                                    }
                                    if (cPots) {
                                        bank.withdraw(gap(combatPots), 1);
                                        sleep(random(800, 1000));
                                    }

                                    if (dAnti) {
                                        bank.withdraw(gap(antiPots), 1);
                                        sleep(random(800, 1000));
                                    }
                                    if (oPots) {
                                        bank.withdraw(gap(overloads), 1);
                                        sleep(random(800, 1000));
                                    }
                                    if (useSummon) {
                                        if (!inventory.contains(finalPouch)) {
                                            if (bank.getItem(finalPouch) == null) {
                                                log("Out of summoning items, stopping the use of summoning");
                                                useSummon = false;
                                            } else {
                                                bank.withdraw(finalPouch, 1);
                                                sleep(random(800, 1000));
                                            }
                                        }

                                        if (!inventory.containsOneOf(summonPots)) {
                                            bank.withdraw(gap(summonPots), 1);
                                            sleep(random(800, 1000));
                                        }
                                    }
                                    if (bank.close()) {
                                        sleep(random(600, 700));
                                        if (!takePots) {
                                            if (dPots) {
                                                if (didPot(strengthPots)) {
                                                    sleep(random(800, 1000));
                                                }
                                                if (didPot(attackPots)) {
                                                    sleep(random(800, 1000));
                                                }
                                            }
                                            if (rPots) {
                                                if (didPot(rangedPots)) {
                                                    sleep(random(800, 1000));
                                                }
                                            }
                                            if (cPots) {
                                                if (didPot(combatPots)) {
                                                    sleep(random(800, 1000));
                                                }
                                            }
                                            if (oPots) {
                                                while (getMyPlayer().getHPPercent() != 100) {
                                                    if (inventory.getCount(food) == 0) {
                                                        break;
                                                    } else {
                                                        inventory.getItem(food).doAction("Eat");
                                                        sleep(random(800, 1000));
                                                    }
                                                }
                                                if (didPot(overloads)) {
                                                    sleep(random(800, 1000));
                                                }
                                                inventory.getItem(food).doAction("Eat");
                                                sleep(random(800, 1000));
                                            }
                                            if (dPots || cPots) {
                                                if (bank.open()) {
                                                    while (!bank.getInterface().isValid()) {
                                                        sleep(random(1500, 2000));
                                                        bank.open();
                                                    }
                                                }
                                                if (bank.isOpen()) {
                                                    if (!inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3]) && dAnti) {
                                                        bank.depositAllExcept(fallytab, dusty, food, finalPouch, summonPots[0], summonPots[1], summonPots[2], summonPots[3]);

                                                        if (bank.getCurrentTab() != tab) {
                                                            openBankTab(tab);
                                                            sleep(random(1000, 1800));
                                                        }
                                                        bank.withdraw(gap(antiPots), 1);
                                                        sleep(random(500, 600));
                                                    } else {
                                                        bank.depositAllExcept(fallytab, dusty, finalPouch, food,
                                                                antiPots[0], antiPots[1], antiPots[2], antiPots[3],
                                                                summonPots[0], summonPots[1], summonPots[2], summonPots[3]);
                                                        sleep(random(500, 600));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } catch (NullPointerException k) {
                                return 100;
                            }
                        }
                    }

                }
            }
        } else if (isInArea(new int[]{2936, 3352, 2942, 3376})) {
            stat = "Walking to wall";
            RSObject a = objects.getNearest(agility1);
            if (a != null) {
                if (calc.distanceTo(a) <= 4) {
                    camera.turnTo(a);
                    a.doAction("Climb");
                    waitToMove();
                } else {
                    walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
                }
            } else {
                walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
            }
        } else if (isInArea(new int[]{2878, 3350, 2943, 3422})) {
            stat = "Walking to dungeon";
            RSObject l = objects.getNearest(55404);
            if (l != null) {
                if (l.isOnScreen()) {
                    l.doAction("Climb");
                    sleep(random(800, 1200));
                } else {
                    walkPathMM(agilitysctotravdung);
                    while (getMyPlayer().isMoving() && calc.distanceTo(walking.getDestination()) >= 4) {
                        sleep(random(100, 200));
                    }
                }
            } else {
                walkPathMM(agilitysctotravdung);
            }
        } else if (isInArea(new int[]{2881, 9794, 2887, 9801}) && shortcut) {
            RSObject h = objects.getNearest(9293);
            if (h != null) {
                if (h.isOnScreen()) {
                    h.doAction("Squeeze");
                    sleep(random(3000, 4000));
                    waitToMove();
                } else {
                    camera.turnTo(h);
                }
            }
        } else if (isInArea(new int[]{2888, 9785, 2924, 9813})) {
            if (needToChargeSummon()) {
                try {
                    walking.walkTileMM(walking.getPath(summonRecharge).getNext());
                } catch (Exception e) {
                    return 100;
                }
                waitToMove();
                RSObject obelisk = objects.getNearest(29947);
                if (obelisk != null) {
                    obelisk.doAction("Renew-points");
                    sleep(random(1000, 1500));
                }
                return 100;
            }
            if (isInArea(new int[]{2888, 9786, 2892, 9790})) {
                RSTile d = new RSTile(2894, 9800);
                walking.walkTileMM(d);
            }
            stat = "Fighting drags";
            if (dungeon) {
                try {
                    RSObject d = objects.getNearest(52852);
                    if (d != null) {
                        if (d.isOnScreen()) {
                            d.doAction("Enter");
                            sleep(random(1500, 2200));
                        } else {
                            walking.walkTileMM(walking.getPath(new RSTile(2911, 9808)).getNext());
                            sleep(random(600, 700));
                        }
                    } else {
                        walking.walkTileMM(walking.getPath(new RSTile(2911, 9808)).getNext());
                        sleep(random(600, 700));
                    }
                } catch (NullPointerException e) {
                    return 100;
                }
            } else {
                fight();
            }
        } else if ((Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0)) {
            fight();
        } else if (!shortcut && !isInArea(new int[]{2889, 9793, 2923, 9813})) {
            RSObject gate = objects.getNearest(2623);
            if (gate == null) {
                walkPath(LADDERS_TO_GATE_ENTRANCE);
                sleep(random(1200, 1300));
            } else if (!gate.isOnScreen()) {
                walkPath(LADDERS_TO_GATE_ENTRANCE);
                sleep(random(1200, 1300));
            } else {
                waitToMove();
                camera.turnTo(gate);
                RSItem key = inventory.getItem(dusty);
                try {
                    if (key.doAction("Use")) {
                        sleep(500, 1500);
                        mouse.click(gate.getModel().getCentralPoint(), true);
                        sleep(random(3000, 3500));

                    }
                } catch (NullPointerException e) {
                    return 10;
                }
            }
        }
        return 1;
    }
    public void waitToMove() {
        sleep(random(1500, 2000));
        while (getMyPlayer().isMoving()) {
            sleep(random(100, 200));
        }
    }
    public int gap(int[] pots) {
        try {
            if (bank.isOpen()) {
                if (bank.getCount(pots[0]) > 0) {
                    return pots[0];
                } else if (bank.getCount(pots[1]) > 0) {
                    return pots[1];
                } else if (bank.getCount(pots[2]) > 0) {
                    return pots[2];
                } else if (bank.getCount(pots[3]) > 0) {
                    return pots[3];
                }
            } else {
                return 0;
            }
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }
    private boolean isInArea(int[] xy) {//Creds Ownageful
        final int x = getMyPlayer().getLocation().getX();
        final int y = getMyPlayer().getLocation().getY();
        if (x >= xy[0] && x <= xy[2] && y >= xy[1] && y <= xy[3]) {
            return true;
        } else {
            return false;

        }
    }
    @Override
    public void onFinish() {
        env.takeScreenshot(true);
        this.stopScript();
    }
    private boolean walkPath(RSTile[] path) {
        if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) <= 2) {
            return walkPathMM(path);
        }
        return false;
    }
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }
    private boolean isInDungeon() {
        return Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0;
    }
    private boolean isInTaverly() {
        return Math.floor(getMyPlayer().getLocation().getY() / 1000) == 9.0;
    }
    private void manageSummoning() {
        try {
            if (useSummon) {
                if (!summoning.isFamiliarSummoned()) {
                    if (summoning.getSummoningPoints() <= 10) {
                        if (didPot(summonPots)) {
                            sleep(random(400, 600));
                            inventory.getItem(finalPouch).doAction("Summon");
                        }
                    } else {
                        inventory.getItem(finalPouch).doAction("Summon");
                    }

                } else if (summoning.getTimeLeft() <= 2) {
                    if (summoning.getSummoningPoints() <= 10) {
                        if (didPot(summonPots)) {
                            sleep(random(400, 600));
                            summoning.doRenewFamiliar();
                        }
                    } else {
                        summoning.doRenewFamiliar();
                    }
                }

            }
        } catch (NumberFormatException e) {
        }
    }
    private void storeSummon(boolean foodQuery) {
        if (summoning.isFamiliarSummoned()) {
            try {
                if (summoning.getFamiliar().canStore()) {
                    RSItem toStore = inventory.getItem(loots[0], hideLoots[0]);
                    if (toStore != null) {
                        if (toStore.doAction("Use")) {
                            mouse.click(summoning.getFamiliar().getNPC().getScreenLocation(), false);
                            if (menu.isOpen()) {
                                if (menu.doAction(" -> " + summoning.getFamiliar().getName())) {
                                    sleep(random(300, 340));
                                }
                            }
                        }
                    }
                } else if (foodQuery == false) {
                    try {
                        inventory.getItem(fallytab).doAction("Break");
                        nBank = true;
                    } catch (NullPointerException e) {
                        log("Out of fally teleport.");
                        stopScript();
                    }
                    sleep(random(7000, 8500));
                }
            } catch (NullPointerException e) {
            }
        }
    }
    private void minorChecks() {

        try {
            if (interfaces.get(671).isValid()) {
                if (interfaces.get(671).getComponent(13).isValid()) {
                    interfaces.get(671).getComponent(13).doClick();
                }
            }

            manageSummoning();
            foodChecking();

            if (nBank
                    && (isInArea(new int[]{2888, 9786, 2892, 9790})
                    || (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0))) {
                inventory.getItem(fallytab).doAction("Break");
                sleep(random(7000, 8500));
            }

            try {
                if (inventory.containsOneOf(drop)) {
                    for (int i = 0; i < drop.length; i++) {
                        if (inventory.contains(drop[i])) {
                            try {
                                inventory.getItem(drop[i]).doAction("Drop");
                            } catch (Exception e) {
                            }
                        }
                    }
                }

                if (!lCharms) {
                    if (inventory.containsOneOf(charms)) {
                        try {
                            inventory.getItem(charms).doAction("Drop");
                        } catch (NullPointerException e) {
                        }
                    }
                }

            } catch (NullPointerException e) {
            }

            if (inventory.isItemSelected()) {
                inventory.getSelectedItem().doAction("Cancel");
            }

            camera.setPitch(true);

            if (walking.getEnergy() > random(20, 40)) {
                walking.setRun(true);
            }
        } catch (NullPointerException e) {
        }
    }
    private void foodChecking() {
        if (game.isLoggedIn()) {
            if (getMyPlayer().getHPPercent() < (random(eatAt - 5, eatAt + 5))) {
                if (!usingFood) {
                    log("Low on health and not using food, stopping script.");
                    inventory.getItem(fallytab).doAction("Break");
                    sleep(random(7000, 8500));
                    stopScript();
                } else {
                    if (inventory.contains(food)) {
                        inventory.getItem(food).doAction("Eat");
                        sleep(random(3900, 3950));
                    } else {
                        inventory.getItem(fallytab).doAction("Break");
                        sleep(random(9000, 9500));
                        nBank = true;
                    }
                }
            }
        }
    }
    private void threadStart() {
        Thread a = new Thread() {
            @Override
            public void run() {
                /**
                 * Check to see if fally tabs exist in inventory on startup
                 */
                if (inventory.getCount(fallytab) == 0) {
                    log("No fally tabs found, please start with fally tabs in inventory.");
                    gui.dispose();
                    nStop = true;
                }
                /**
                 * Check to see if we have agility level for the shortcut
                 */
                if (skills.getRealLevel(Skills.AGILITY) >= 70) {
                    shortcut = true;
                } else {
                    shortcut = false;
                    if (!inventory.contains(dusty)) {
                        log("Dusty Key Not Found! Stopping Script, please start with Dusty Key in inventory.");
                        gui.dispose();
                        nStop = true;
                    }
                }
                /**
                 * Check to see if we dungeoneering level for resource dungeon
                 */
                if (skills.getRealLevel(Skills.DUNGEONEERING) >= 60) {
                    dungeon = true;
                } else {
                    dungeon = false;
                }
                for (int i = 0; i < prices.length; i++) {
                    try {
                        prices[i] = grandExchange.lookup(loots[i]).getGuidePrice();
                    } catch (NullPointerException e) {
                        prices[i] = 0;
                    }
                    log("Loading Prices for Item:  " + loots[i] + ", Price: " + prices[i]);
                }
                for (int i = 0; i < hidePrices.length; i++) {
                    hidePrices[i] = grandExchange.lookup(hideLoots[i]).getGuidePrice();
                    log("Loading Prices for Item:  " + hideLoots[i] + ", Price: " + hidePrices[i]);
                }
                log("Done loading prices.");
                interrupt();
            }
        };
        a.start();
    }
    private boolean needToChargeSummon() {
        if (dungeon) {
            return skills.getCurrentLevel(Skills.SUMMONING) != skills.getRealLevel(Skills.SUMMONING) && useSummon;
        } else {
            return skills.getCurrentLevel(Skills.SUMMONING) <= 10 && useSummon;
        }
    }
    private enum Skill {
        ATTACK(Skills.ATTACK, "Attack", 0),
        STRENGTH(Skills.STRENGTH, "Strength", 1),
        DEFENCE(Skills.DEFENSE, "Defence", 2),
        CONSTITUTION(Skills.CONSTITUTION, "Constitution", 3),
        RANGE(Skills.RANGE, "Range", 4);
        int skillID;
        String skillName;
        int index;
        private Skill(int skillID, String skillName, int index) {
            this.skillID = skillID;
            this.skillName = skillName;
            this.index = index;
        }
    }
    private final Image closed = getImage("http://ownagebots.com/bdk2.png");
    private final Image tabOne = getImage("http://ownagebots.com/bdk1.png");
    private final Rectangle hideRect = new Rectangle(477, 336, 34, 37);
    private final Rectangle tabOneRect = new Rectangle(327, 336, 148, 37);
    public void onRepaint(Graphics g1) {
        if (game.isLoggedIn()) {
            Graphics2D g = (Graphics2D) g1;
            st = tm.toElapsedString();
            //totalLoot = ((totalBone + storedBones + inventory.getCount(loots[0]) - ivenBone) * prices[0])
            //		+ ((totalHide + storedHides + inventory.getCount(loots[1]) - ivenHide) * prices[1]);
            lph = (int) (totalLoot / ((Double.parseDouble(st.substring(0, 2))) + (Double.parseDouble(st.substring(3, 5)) / 60)
                    + (Double.parseDouble(st.substring(6, 8)) / 3600)));

            if (ptab == 1) {
                g.drawImage(tabOne, 0, 288, null);
                drawSkillBars(g);
                drawString(g, "Time Running: " + st, 290, 400);
                drawString(g, "Status: " + stat, 290, 415);
                drawString(g, "Total Loot: " + totalLoot, 290, 430);
                drawString(g, "Loot Per Hour: " + lph, 290, 445);
                drawString(g, "Bank Runs: " + bankRun, 290, 460);
                drawString(g, "Version: " + version, 290, 475);

            } else {
                g.drawImage(closed, 0, 288, null);
            }
            drawMouse(g);
        }
    }
    private void drawString(Graphics g, String s, int x, int y) {
        g.setColor(new Color(90, 15, 15));
        g.setFont(new Font("Serif", 0, 12));
        g.drawString(s, x, y);
        g.setColor(new Color(255, 255, 255, 90));
        g.drawString(s, x + 1, y + 1);
    }
    private void drawSkillBars(Graphics g) {
        for (Skill s : Skill.values()) {
            int x1 = s.index <= 4 ? 20 : 180;
            int y1 = s.index <= 4 ? 360 + (s.index * 20) : 360 + ((s.index - 4) * 20);
            g.setColor(new Color(90, 15, 15, 100));
            g.drawRect(x1, y1, 250, 15);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(x1, y1, (int) (skills.getPercentToNextLevel(s.skillID) * 2.5), 15);
            g.setColor(new Color(90, 15, 15));
            g.setFont(new Font("Serif", 0, 12));
            g.drawString(s.skillName + ": " + skills.getPercentToNextLevel(s.skillID)
                    + "% to level " + (skills.getRealLevel(s.skillID) + 1) + ")", x1 + 6, y1 + 13);
            g.setColor(new Color(255, 255, 255, 90));
            g.drawString(s.skillName + ": " + skills.getPercentToNextLevel(s.skillID)
                    + "% to level " + (skills.getRealLevel(s.skillID) + 1) + ")", x1 + 7, y1 + 14);
        }
    }
    public void mouseClicked(MouseEvent e) {
        if (hideRect.contains(e.getPoint())) {
            ptab = 3;
        } else if (tabOneRect.contains(e.getPoint())) {
            ptab = 1;
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
            if (inventory.getItem(pots).doAction("Drink")) {
                return true;
            }
        }
        return false;
    }
    private void performHumanAction() {//RawRs antiban
        int randomNum = random(1, 30);
        int r = random(1, 35);
        if (randomNum == 6) {
            if (r == 1) {
                if (game.getCurrentTab() != game.TAB_STATS) {
                    game.openTab(game.TAB_STATS);
                    mouse.move(random(680, 730), random(355, 370));
                    sleep(random(1000, 1500));
                }
            }
            if (r == 2) {
                game.openTab(random(1, 14));
            }
            if (r == 3) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 4) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 5) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 6) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 7) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 8) {
                camera.setAngle(random(100, 360));
            }
            if (r == 9) {
                camera.setAngle(random(100, 360));
            }
            if (r == 10) {
                camera.setAngle(random(100, 360));
            }
        }
    }
    private void drawMouse(final Graphics g) {
        final Point loc = mouse.getLocation();
        if (System.currentTimeMillis()
                - mouse.getPressTime() < 500) {
            g.setColor(Color.PINK);
            g.fillOval(loc.x - 5, loc.y - 5, 10, 10);
        } else {
            g.setColor(Color.GREEN);
        }
        g.drawLine(0, loc.y, game.getWidth(), loc.y);
        g.drawLine(loc.x, 0, loc.x, game.getHeight());
    }
    private int fight() {
        try {
            if (nanti) {
                if (inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3])) {
                    if (inventory.getItem(antiPots).doAction("Drink")) {
                        sleep(random(3900, 3950));
                        nanti = false;
                        return 20;
                    }
                }
            }

            if (inventory.isFull() && inventory.getCount(food) == 0) {
                if (useSummon && !summonFull && summoning.isFamiliarSummoned()) {
                    storeSummon(false);
                } else {
                    try {
                        inventory.getItem(fallytab).doAction("Break");
                        nBank = true;
                        return 100;
                    } catch (NullPointerException e) {
                        log("Out of fally teleport.");
                        stopScript();
                    }
                    sleep(random(7000, 8500));
                }
            }
            foodChecking();
            if (takePots) {
                doPots();
            }
            if (lHides) {
                lootzh = groundItems.getNearest(hideLoots);
                if (lootzh != null) {
                    if (calc.tileOnScreen(lootzh.getLocation())) {
                        if (inventory.isFull() && inventory.contains(food)) {
                            if (useSummon && !summonFull) {
                                storeSummon(true);
                            }
                            sleep(random(800, 900));
                            if (inventory.isFull()) {
                                inventory.getItem(food).doAction("Eat");
                                sleep(random(1200, 1300));
                            }
                        }
                        if (inventory.isFull()) {
                            inventory.getItem(food).doAction("Eat");
                            sleep(random(1200, 1300));
                        }
                        int startHide = inventory.getCount(hideLoots[0]);
                        lootzh.doAction("Take Blue dragon");
                        while (getMyPlayer().isMoving()) {
                            sleep(200, 400);
                        }
                        if (inventory.getCount(hideLoots[0]) > startHide) {
                            totalLoot += hidePrices[0];
                        }
                        return 100;
                    } else {
                        if (calc.tileOnMap(lootzh.getLocation())) {
                            walking.walkTileMM(lootzh.getLocation());
                            while (getMyPlayer().isMoving()) {
                                sleep(200, 400);
                            }
                            return 100;
                        }
                    }
                }
            }
            if (lCharms) {
                charmz = groundItems.getNearest(charms);
                if (charmz != null) {
                    if (calc.tileOnScreen(charmz.getLocation())) {
                        if (inventory.isFull() && inventory.contains(food) && !inventory.contains(charmz.getItem().getID())) {
                            if (useSummon && !summonFull) {
                                storeSummon(true);
                            }
                            sleep(random(800, 900));
                            if (inventory.isFull()) {
                                inventory.getItem(food).doAction("Eat");
                                sleep(random(100, 300));
                            }
                        }
                        if (inventory.isFull()) {
                            inventory.getItem(food).doAction("Eat");
                            sleep(random(1200, 1300));
                        }
                        charmz.doAction("charm");
                        sleep(600, 850);
                        while (getMyPlayer().isMoving()) {
                            sleep(1200, 1400);
                        }
                        return 100;
                    } else {
                        if (calc.tileOnMap(charmz.getLocation())) {
                            walking.walkTileMM(charmz.getLocation());
                            while (getMyPlayer().isMoving()) {
                                sleep(200, 400);
                            }
                            return 100;
                        }
                    }
                }
            }
            lootzd = groundItems.getNearest(loots[0]);
            if (lootzd != null) {
                if (calc.tileOnScreen(lootzd.getLocation())) {
                    if (inventory.isFull() && inventory.contains(food)) {
                        if (useSummon && !summonFull) {
                            storeSummon(true);
                        }
                        sleep(random(800, 900));
                        if (inventory.isFull()) {
                            inventory.getItem(food).doAction("Eat");
                            sleep(random(1200, 1300));
                        }
                    }
                    if (inventory.isFull()) {
                        inventory.getItem(food).doAction("Eat");
                        sleep(random(1200, 1300));
                    }
                    int startBone = inventory.getCount(loots[0]);
                    lootzd.doAction("Take Drag");

                    sleep(600, 850);
                    while (getMyPlayer().isMoving()) {
                        sleep(200, 400);
                    }
                    if (inventory.getCount(loots[0]) > startBone) {
                        totalLoot += prices[0];
                    }
                    return 100;
                } else {
                    if (calc.tileOnMap(lootzd.getLocation())) {
                        walking.walkTileMM(lootzd.getLocation());
                        while (getMyPlayer().isMoving()) {
                            sleep(200, 400);
                        }
                        return 100;
                    }
                }
            }

            lootz = groundItems.getNearest(loots);
            if (lootz != null) {
                for (int i = 0; i < loots.length; i++) {
                    if (lootz.getItem().getID() == loots[i]) {
                        if (calc.tileOnScreen(lootz.getLocation())) {
                            if (inventory.isFull() && inventory.contains(food)) {
                                if (useSummon && !summonFull) {
                                    storeSummon(true);
                                }
                                sleep(random(800, 900));
                                if (inventory.isFull()) {
                                    inventory.getItem(food).doAction("Eat");
                                    sleep(random(1200, 1300));
                                }
                            }
                            if (inventory.isFull()) {
                                inventory.getItem(food).doAction("Eat");
                                sleep(random(1200, 1300));
                            }
                            int startItem = inventory.getCount(loots[i]);
                            lootz.doAction("Take " + names[i]);
                            sleep(600, 850);
                            while (getMyPlayer().isMoving()) {
                                sleep(200, 400);
                            }
                            if (inventory.getCount(loots[i]) > startItem) {
                                totalLoot += prices[i];
                            }
                            return 100;
                        } else {
                            if (calc.tileOnMap(lootz.getLocation())) {
                                walking.walkTileMM(lootz.getLocation());
                                while (getMyPlayer().isMoving()) {
                                    sleep(200, 400);
                                }
                                return 100;
                            }
                        }
                    }
                }
            }

            if (dungeon) {
                RSCharacter inter = getMyPlayer().getInteracting();
                if (inter != null) {
                    if (inter.getName().equals("Blue dragon")) {
                        if (inter.getAnimation() == 12250) {
                            long startWait = System.currentTimeMillis();
                            boolean storedOnce = false;
                            while (System.currentTimeMillis() - startWait < 4350) {
                                if (!storedOnce) {
                                    if (useSummon && !summonFull) {
                                        storeSummon(true);
                                    }
                                    storedOnce = true;
                                }
                            }
                            return 100;
                        } else {
                            sleep(random(200, 300));
                        }
                    }
                } else {
                    if (!getMyPlayer().isInCombat()) {
                        drag = npcs.getNearest(dungeonFilter);
                        if (drag != null) {
                            if (drag.isOnScreen()) {
                                try {
                                    RSModel dragModel = drag.getModel();
                                    if (dragModel != null) {
                                        try {
                                            dragModel.doAction("Attack");
                                        } catch (NullPointerException e) {
                                            log.info("Dragon model is null.");
                                            sleep(random(400, 500));
                                        }
                                    } else {
                                        drag.doAction("Attack");
                                        sleep(random(400, 500));
                                    }
                                    sleep(random(1500, 2000));
                                    while (getMyPlayer().isMoving()) {
                                        sleep(200, 400);
                                    }
                                } catch (NullPointerException e) {
                                    return 100;
                                }
                            } else {
                                try {
                                    walking.walkTileMM(drag.getLocation());
                                    sleep(random(600, 700));
                                    while (getMyPlayer().isMoving()) {
                                        RSModel dragModel = drag.getModel();
                                        if (drag.isOnScreen()) {
                                            if (dragModel != null) {
                                                try {
                                                    dragModel.doAction("Attack");
                                                    sleep(random(400, 500));
                                                } catch (NullPointerException e) {
                                                    log.info("Dragon model is null.");
                                                }
                                            } else {
                                                drag.doAction("Attack");

                                                sleep(random(400, 500));
                                            }
                                        }
                                        sleep(200, 400);
                                    }
                                } catch (NullPointerException e) {
                                    return 100;
                                }
                            }
                        } else {
                            if (dungeon) {
                                try {
                                    if (calc.distanceTo(new RSTile(987, 4502)) >= 6) {
                                        walkPathMM(centerDung);
                                        sleep(random(600, 700));
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                        return 10;
                    }
                    return 10;
                }
            } else {
                RSNPC blueDragon = npcs.getNearest(outsideFilter);
                if (!getMyPlayer().isInCombat() && getMyPlayer().isIdle()
                        && getMyPlayer().getAnimation() == -1
                        && getMyPlayer().getInteracting() == null) {
                    if (blueDragon != null) {
                        if (blueDragon.isOnScreen()) {
                            blueDragon.doAction("Attack");
                            sleep();
                        } else {
                            camera.turnTo(blueDragon.getLocation());
                        }
                    } else {
                        camera.moveRandomly(30);
                        if (calc.distanceTo(center) > 6) {
                            walking.walkTileMM(center);
                            sleep();
                        }
                        sleep();
                    }
                }
                return 10;
            }
            return 10;
        } catch (NullPointerException e) {
            return 10;
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
        return walkPathMM(path, maxDist, 0, 0);
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
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    private void sleep() {
        sleep(500, 1000);
    }
    private void doPots() {
        try {
            if (dPots) {
                if (skills.getCurrentLevel(Skills.ATTACK) == skills.getRealLevel(Skills.ATTACK)
                        || ((skills.getRealLevel(Skills.ATTACK) > 99) && (skills.getCurrentLevel(Skills.ATTACK) <= 99))) {
                    if (inventory.containsOneOf(attackPots)) {
                        doInventoryItem(attackPots, "Drink");
                        if (waitForAnim(829) != -1) {
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(random(3300, 3600));
                            }
                        }
                    }
                }
                if (skills.getCurrentLevel(Skills.STRENGTH) == skills.getRealLevel(Skills.STRENGTH)
                        || ((skills.getRealLevel(Skills.STRENGTH) > 99) && (skills.getCurrentLevel(Skills.STRENGTH) <= 99))) {
                    if (inventory.containsOneOf(strengthPots)) {
                        doInventoryItem(strengthPots, "Drink");
                        if (waitForAnim(829) != -1) {
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(random(3300, 3600));
                            }
                        }
                    }
                }
            }

            if (cPots) {
                if (skills.getCurrentLevel(Skills.STRENGTH) == skills.getRealLevel(Skills.STRENGTH)
                        || ((skills.getRealLevel(Skills.STRENGTH) > 99) && (skills.getCurrentLevel(Skills.STRENGTH) <= 99))) {
                    if (inventory.containsOneOf(strengthPots)) {
                        doInventoryItem(combatPots, "Drink");
                        if (waitForAnim(829) != -1) {
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(random(3300, 3600));
                            }
                        }
                    }
                }
            }

            if (oPots) {
                if (skills.getCurrentLevel(Skills.STRENGTH) == skills.getRealLevel(Skills.STRENGTH)
                        || ((skills.getRealLevel(Skills.STRENGTH) > 99) && (skills.getCurrentLevel(Skills.STRENGTH) <= 99))) {

                    if (inventory.containsOneOf(overloads)) {
                        while (getMyPlayer().getHPPercent() != 100) {
                            if (inventory.getCount(food) == 0) {
                                break;
                            } else {
                                inventory.getItem(food).doAction("Eat");
                                sleep(random(800, 1000));
                            }
                        }
                        doInventoryItem(overloads, "Drink");
                        if (waitForAnim(829) != -1) {
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(random(3300, 3600));
                            }
                        }
                        inventory.getItem(food).doAction("Eat");
                        sleep(random(800, 1000));
                    }
                }
            }

            if (rPots) {
                if (skills.getCurrentLevel(Skills.RANGE) == skills.getRealLevel(Skills.RANGE)
                        || ((skills.getRealLevel(Skills.RANGE) > 99) && (skills.getCurrentLevel(Skills.RANGE) <= 99))) {
                    if (inventory.containsOneOf(rangedPots)) {
                        doInventoryItem(rangedPots, "Drink");
                        if (waitForAnim(829) != -1) {
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(random(3300, 3600));
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }
    private boolean doInventoryItem(int[] ids, String action) {
        ArrayList<RSComponent> possible = new ArrayList<RSComponent>();
        for (RSComponent com : inventory.getInterface().getComponents()) {
            for (int i : ids) {
                if (i == com.getComponentID()) {
                    possible.add(com);
                }
            }
        }
        if (possible.isEmpty()) {
            return false;
        }
        RSComponent winner = possible.get(random(0,
                possible.size() - 1));
        Rectangle loc = winner.getArea();
        mouse.move(
                (int) loc.getX() + 3, (int) loc.getY() + 3, (int) loc.getWidth() - 3, (int) loc.getHeight() - 3);
        this.sleep(
                random(100, 300));
        String top = menu.getItems()[0].toLowerCase();
        if (top.contains(action.toLowerCase())) {
            mouse.click(true);
            return true;

        } else if (menuContains(action)) {
            return menu.doAction(action);
        }
        return false;
    } //Credits to foul.
    private boolean menuContains(String item) {
        try {
            for (String s : menu.getItems()) {
                if (s.toLowerCase().contains(item.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return menuContains(item);
        }
        return false;
    }
    public int waitForAnim(int timeout) {
        long start = System.currentTimeMillis();
        int anim = -1;

        while (System.currentTimeMillis() - start < timeout) {
            if ((anim = getMyPlayer().getAnimation()) != -1) {
                break;
            }
            this.sleep(15);
        }
        return anim;
    }
    public void messageReceived(MessageEvent me) {
        String str = me.getMessage().toString();
        if (str.contains("fiery breath") && dAnti) {
            nanti = true;
        } else if (str.contains("Your familiar cannot") && useSummon) {
            summonFull = true;
        } else if (str.contains("else is fighting") && useSummon) {
            if (dungeon) {
                drag = npcs.getNearest(dungeonFilter);
            } else {
                drag = npcs.getNearest(outsideFilter);
            }
        }
    }
    class BDKGUI extends javax.swing.JFrame {
        public BDKGUI() {
            initComponents();
            this.setResizable(false);
            this.setLocation(520, 250);
        }
        private void initComponents() {

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
            jLabel4 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            jLabel8 = new javax.swing.JLabel();
            jSeparator3 = new javax.swing.JSeparator();
            jCheckBox7 = new javax.swing.JCheckBox();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel9 = new javax.swing.JLabel();
            jSeparator4 = new javax.swing.JSeparator();
            jLabel10 = new javax.swing.JLabel();
            jSeparator5 = new javax.swing.JSeparator();
            jLabel11 = new javax.swing.JLabel();
            jLabel12 = new javax.swing.JLabel();
            jCheckBox6 = new javax.swing.JCheckBox();
            jCheckBox8 = new javax.swing.JCheckBox();
            jCheckBox9 = new javax.swing.JCheckBox();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jRadioButton3 = new javax.swing.JRadioButton();
            jRadioButton4 = new javax.swing.JRadioButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Ownage Blue Dragon Killer PRO");
            setAlwaysOnTop(true);
            setBackground(new java.awt.Color(255, 255, 255));
            setFocusable(false);

            jLabel5.setForeground(new java.awt.Color(0, 153, 51));
            jLabel5.setText("Food:");

            jLabel1.setText("Food ID:");

            jLabel2.setText("Withdraw:");

            jLabel6.setText("Eat At:");

            jSpinner1.setModel(new javax.swing.SpinnerNumberModel(40, 20, 90, 1));

            jLabel7.setForeground(new java.awt.Color(204, 0, 102));
            jLabel7.setText("Pots:");

            jCheckBox2.setText("Drink Antifire");

            jCheckBox3.setText("Drink Supers");

            jCheckBox4.setText("Drink Range Pots");

            jCheckBox5.setText("Drink Combat Pots");

            jCheckBox1.setText("Loot Charms?");

            jLabel4.setText("Fally Teleport Tabs, and Dusty Key (if not 70 agility), food in first slot (if Using)");

            jLabel3.setText("Start script in fally bank with:");

            jButton1.setText("Start");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jLabel8.setForeground(new java.awt.Color(0, 51, 255));
            jLabel8.setText("Summoning:");

            jCheckBox7.setText("Beast of Burden:");

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bull Ant", "TerrorBird", "War Tortoise", "Pack Yak"}));

            jLabel9.setForeground(new java.awt.Color(255, 0, 0));
            jLabel9.setText("Looting:");

            jLabel10.setForeground(new java.awt.Color(255, 0, 255));
            jLabel10.setText("Finalize:");

            jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18));
            jLabel11.setForeground(new java.awt.Color(0, 102, 255));
            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Ownageful's Blue Dragon Killer Pro");

            jLabel12.setText("%");

            jCheckBox6.setText("Loot Dragon hides?");

            jCheckBox8.setText("Drink Overloads");

            jCheckBox9.setText("Take Pots to Dragons");

            jRadioButton1.setText("Green");

            jRadioButton2.setText("Blue");

            jRadioButton3.setText("Gold");

            jRadioButton4.setText("Crimson");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jCheckBox8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox9)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jCheckBox2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox3)).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox5))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel9)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel7)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel5)).addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jCheckBox7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox1).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton3)).addGroup(layout.createSequentialGroup().addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jRadioButton4))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE).addComponent(jCheckBox6).addGap(27, 27, 27)));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addGap(3, 3, 3).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addGap(26, 26, 26).addComponent(jLabel7).addGap(1, 1, 1).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox2).addComponent(jCheckBox4).addComponent(jCheckBox3).addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox8).addComponent(jCheckBox9)).addGap(7, 7, 7).addComponent(jLabel8).addGap(1, 1, 1).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox7).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE).addComponent(jLabel9).addGap(1, 1, 1).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox1).addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10).addGap(1, 1, 1).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel3).addGap(1, 1, 1).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(7, 7, 7)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(90, 90, 90)))));

            pack();
        }
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            if (jCheckBox1.isSelected()) {
                lCharms = true;
                if (jRadioButton1.isSelected()) {
                    charmsAL.add(12159);//green
                }
                if (jRadioButton2.isSelected()) {
                    charmsAL.add(12163);//blue
                }
                if (jRadioButton3.isSelected()) {
                    charmsAL.add(12158);//gold
                }
                if (jRadioButton4.isSelected()) {
                    charmsAL.add(12160);//crimson
                }

                charms = new int[charmsAL.size()];
                for (int i = 0; i < charms.length; i++) {
                    charms[i] = charmsAL.get(i);
                    log("Adding charm " + charms[i] + " to loot.");
                }
            }

            if (jCheckBox2.isSelected()) {
                dAnti = true;
            }
            if (jCheckBox3.isSelected()) {
                dPots = true;
            }
            if (jCheckBox4.isSelected()) {
                rPots = true;
            }
            if (jCheckBox5.isSelected()) {
                cPots = true;
            }
            if (jCheckBox8.isSelected()) {
                oPots = true;
            }
            if (jCheckBox6.isSelected()) {
                lHides = true;
            }
            if (jCheckBox9.isSelected()) {
                takePots = true;
            }
            eatAt = (Integer) jSpinner1.getValue();
            if (jCheckBox7.isSelected()) {
                useSummon = true;
                int a = jComboBox1.getSelectedIndex();
                finalPouch = bobPouchIDS[a];
            }
            try {
                food = Integer.parseInt(jTextField1.getText());
                withdraw = Integer.parseInt(jTextField2.getText());
                usingFood = true;
            } catch (NumberFormatException numberFormatException) {
            }

            if (jTextField1.getText().isEmpty()) {
                usingFood = false;
            }
            this.dispose();
        }
        private javax.swing.JButton jButton1;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox2;
        private javax.swing.JCheckBox jCheckBox3;
        private javax.swing.JCheckBox jCheckBox4;
        private javax.swing.JCheckBox jCheckBox5;
        private javax.swing.JCheckBox jCheckBox7;
        private javax.swing.JCheckBox jCheckBox6;
        private javax.swing.JCheckBox jCheckBox8;
        private javax.swing.JCheckBox jCheckBox9;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JSeparator jSeparator2;
        private javax.swing.JSeparator jSeparator3;
        private javax.swing.JSeparator jSeparator4;
        private javax.swing.JSeparator jSeparator5;
        private javax.swing.JRadioButton jRadioButton1;
        private javax.swing.JRadioButton jRadioButton2;
        private javax.swing.JRadioButton jRadioButton3;
        private javax.swing.JRadioButton jRadioButton4;
        private javax.swing.JSpinner jSpinner1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
    }
}
