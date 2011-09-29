import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.rsbot.Configuration;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Methods;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;
@ScriptManifest(authors = {"Ownageful"}, name = "Swift Guild Ranger", version = 1.0, description = "Plays the ranger guild minigame.")
public class SwiftGuildRanger extends Script implements PaintListener, MessageListener, MouseListener {
    RSObject target, ladder;
    RSNPC merchant;
    Point p;
    int count, gp = 0, it, te, eg, ie, ms, merch = 694;
    double eph, xptl;
    int x, y, combatCounter = 0, ticket = -1;
    int[] bound = {2669, 3417, 2675, 3420};
    boolean toPaint = true, startScript = false, needLog = false;
    boolean bought = false, food = false, arrows = false, logout = false, buy = false, returnToArea = true;
    Timer tm = new Timer(0);
    double ptl;
    String st, status, ps = "Hide Paint";
    String ttl;
    int ladderID = 2511;
    Image bg;
    Methods m = new Methods();
    SwiftGui gui;
    final int[] foodID = {1895, 1893, 1891, 4293, 2142, 291, 2140, 3228, 9980,
        7223, 6297, 6293, 6295, 6299, 7521, 9988, 7228, 2878, 7568, 2343,
        1861, 13433, 315, 325, 319, 3144, 347, 355, 333, 339, 351, 329,
        3381, 361, 10136, 5003, 379, 365, 373, 7946, 385, 397, 391, 3369,
        3371, 3373, 2309, 2325, 2333, 2327, 2331, 2323, 2335, 7178, 7180,
        7188, 7190, 7198, 7200, 7208, 7210, 7218, 7220, 2003, 2011, 2289,
        2291, 2293, 2295, 2297, 2299, 2301, 2303, 1891, 1893, 1895, 1897,
        1899, 1901, 7072, 7062, 7078, 7064, 7084, 7082, 7066, 7068, 1942,
        6701, 6703, 7054, 6705, 7056, 7060, 2130, 1985, 1993, 1989, 1978,
        5763, 5765, 1913, 5747, 1905, 5739, 1909, 5743, 1907, 1911, 5745,
        2955, 5749, 5751, 5753, 5755, 5757, 5759, 5761, 2084, 2034, 2048,
        2036, 2217, 2213, 2205, 2209, 2054, 2040, 2080, 2277, 2225, 2255,
        2221, 2253, 2219, 2281, 2227, 2223, 2191, 2233, 2092, 2032, 2074,
        2030, 2281, 2235, 2064, 2028, 2187, 2185, 2229, 6883, 1971, 4608,
        1883, 1885, 15272};
    @Override
    public boolean onStart() {
        if (game.isLoggedIn()) {
            tm.reset();
            createAndWaitforGUI();
            mouse.setSpeed(ms);
            try {
                it = inventory.getItem(1464).getStackSize();
            } catch (NullPointerException e) {
                it = 0;
            }
            ie = skills.getCurrentExp(Skills.RANGE);
            try {
                bg = Toolkit.getDefaultToolkit().getImage(new URL("http://www.fileden.com/files/2010/4/18/2831958//ok.png"));
            } catch (MalformedURLException e) {
            }
            target = objects.getNearest(1308);
            if (target != null) {
                p = target.getModel().getPoint();
            }
            return true;
        } else {
            log("Please log in before starting the script.");
            return false;
        }
    }
    private void createAndWaitforGUI() {
        if (SwingUtilities.isEventDispatchThread()) {
            gui = new SwiftGui();
            gui.setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        gui = new SwiftGui();
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
    public int loop() {

        if (needLog) {
            log("Logging out, rune arrows purchased.");
            env.takeScreenshot(false);
            logoutProcedure();
        }

        checkForFood();
        if (logout) {
            try {
                if (inventory.getItem(1464).getStackSize() >= ticket) {
                    if (!buy) {
                        log("Logging out, target tickets reached.");
                        env.takeScreenshot(false);
                        logoutProcedure();
                    } else {
                        returnToArea = false;
                        loop1();
                    }
                    return 10;
                }
            } catch (NullPointerException e) {
            }
        }
        if (random(1, 2500) == random(1, 2500)) {
            antiban();
            return 100;
        }
        try {
            if (game.getPlane() == 2) {
                sleep(random(2000, 3000));
                ladder = objects.getNearest(2512);
                if (ladder != null) {
                    ladder.doAction("Climb-down");
                    sleep(random(1000, 1200));
                }
                combatCounter++;
                return 10;
            }


            if (getMyPlayer().isInCombat()) {
                camera.setPitch(true);
                while (game.getPlane() != 2) {
                    checkForFood();
                    ladder = objects.getNearest(ladderID);
                    if (ladder != null) {
                        if (ladder.isOnScreen()) {
                            ladder.doAction("Climb-up");
                            sleep(random(400, 500));
                        } else {
                            walking.walkTileMM(new RSTile(2667, 3426));
                            sleep(random(1000, 1900));
                        }
                    } else {
                        walking.walkTileMM(new RSTile(2667, 3426));
                        sleep(random(1000, 1900));
                    }
                    if (interfaces.get(564).getComponent(17).isValid()) {
                        interfaces.get(564).getComponent(17).doClick();
                        sleep(random(1500, 2000));
                        return 10;
                    }
                }
                return 10;
            }
        } catch (NullPointerException e) {
        }

        camera.setPitch(false);

        if (target != null && !target.isOnScreen()) {
            camera.turnTo(target);
            sleep(random(1000, 1600));
            p = target.getModel().getPoint();
        }


        if (settings.getSetting(156) > 0 && settings.getSetting(156) < 11) {
            bought = true;
        } else if (settings.getSetting(156) == 11) {
            bought = false;
        }

        try {
            if (inventory.getItem(995).getStackSize() < 200) {
                log("Out of coins, logging out.");
                game.logout(true);
                return -1;
            }

            if (interfaces.get(243).getComponent(4).containsText("Sorry")) {
                bought = false;
            }
        } catch (NullPointerException e) {
        }

        if (!isInArea(bound)) {
            if (returnToArea) {
                status = "Walking to Targets";try {
                    walking.walkTileMM(walking.getPath(new RSTile(2671, 3418)).getNext());
                } catch (Exception e) {
                }
                while (getMyPlayer().isMoving()) {
                    sleep(random(10, 30));
                }
                if (isInArea(bound)) {
                    try {
                        camera.turnTo(target);
                        sleep(random(1500, 2300));
                        p = target.getModel().getPoint();
                    } catch (NullPointerException e) {
                    }
                }
                return 1;
            }
        }


        if (interfaces.get(236).isValid()) {
            if (interfaces.get(236).getComponent(1).doClick()) {
                equipArrows();
                bought = true;
                p = target.getModel().getPoint();
                return 1;
            }
        }

        if (!bought) {
            status = "Buying Game.";
            if (!interfaces.get(325).isValid() && !menu.isOpen()) {
                buyArrows(npcs.getNearest(693));
            } else if (menu.isOpen()) {

                Point mp = mouse.getLocation();
                if (mp.x >= menu.getLocation().x
                        && mp.y >= menu.getLocation().y
                        && !menu.contains("Attack")) {
                    mouse.click(true);
                    sleep(random(700, 800));
                } else {
                    menu.doAction("Fire-at");
                    sleep(random(700, 800));
                }
            } else {
                interfaces.get(325).getComponent(40).doClick();
                sleep(random(800, 900));
            }
            return 1;
        } else {
            target = objects.getNearest(1308);
            status = "Attacking Targets";
            if (target != null) {
                if (!target.isOnScreen()) {
                    camera.turnTo(target);
                    p = target.getModel().getPoint();
                }

                if (interfaces.get(325).isValid()) {
                    if (menu.isOpen()) {
                        Point mp = mouse.getLocation();
                        if (mp.x >= menu.getLocation().x
                                && mp.y >= menu.getLocation().y
                                && !menu.contains("Attack")) {
                            mouse.click(true);
                            sleep(random(700, 800));
                        } else {
                            menu.doAction("Fire-at");
                            sleep(random(700, 800));
                        }
                    } else {
                        if (interfaces.get(325).isValid()) {
                            interfaces.get(325).getComponent(40).doClick();
                            try {
                                p = target.getModel().getPoint();
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                } else if (getMyPlayer().getAnimation() == 426
                        && settings.getSetting(156) > 0
                        && settings.getSetting(156) < 11
                        && !menu.isOpen() && isInArea(bound) && !getMyPlayer().isMoving()
                        && !getMyPlayer().isInCombat() && bought) {
                    if (!menu.isOpen()) {
                        try {
                            mouse.click(p, false);
                            mouse.move(new Point(menu.getLocation().x + (random(10, 50)), menu.getLocation().y + 30));
                        } catch (NullPointerException e) {
                        }
                        long startTiming = System.currentTimeMillis();
                        while (!interfaces.get(325).isValid()) {
                            if (System.currentTimeMillis() - startTiming >= 3000) {
                                log("3 seconds passed, breaking loop");
                                break;
                            }
                            sleep(random(50, 100));
                        }
                    }
                } else if (bought && settings.getSetting(156) > 0 && settings.getSetting(156) < 11
                        && !getMyPlayer().isMoving() && !getMyPlayer().isInCombat()
                        && isInArea(bound) && getMyPlayer().getAnimation() != 426 && !menu.isOpen()) {
                    mouse.click(p, true);
                } else if (bought && settings.getSetting(156) > 0 && settings.getSetting(156) < 11
                        && !getMyPlayer().isMoving() && !getMyPlayer().isInCombat()
                        && isInArea(bound) && getMyPlayer().getAnimation() != 426 && menu.isOpen()) {
                    if (menu.isOpen()) {
                        Point mp = mouse.getLocation();
                        if (mp.x >= menu.getLocation().x
                                && mp.y >= menu.getLocation().y
                                && !menu.contains("Attack")) {
                            mouse.click(true);
                            sleep(random(700, 800));
                        } else {
                            menu.doAction("Fire-at");
                            sleep(random(700, 800));
                        }
                    }
                } else if (interfaces.get(325).isValid()) {
                    interfaces.get(325).getComponent(40).doClick();
                    try {
                        p = target.getModel().getPoint();
                    } catch (NullPointerException e) {
                    }
                }

            }
        }
        return 1;
    }
    public void onRepaint(Graphics g) {
        try {
            te = inventory.getItem(1464).getStackSize() - it;
        } catch (Exception e) {
            te = 0;
        }
        eg = skills.getCurrentExp(Skills.RANGE) - ie;
        st = tm.toElapsedString();
        xptl = skills.getExpToNextLevel(Skills.RANGE);
        ptl = skills.getPercentToNextLevel(Skills.RANGE);
        eph = (eg / ((Double.parseDouble(st.substring(0, 2))) + (Double.parseDouble(st.substring(3, 5)) / 60)
                + (Double.parseDouble(st.substring(6, 8)) / 3600)));
        double hour = (int) Math.floor(xptl / eph);
        double minutes = ((xptl / eph) - hour) * 60;
        double seconds = (int) ((minutes - Math.floor(minutes)) * 60);
        ttl = (int) hour + ":" + (int) minutes + ":" + (int) seconds;

        if (toPaint) {
            drawPaint(g);
            drawBox(g, 5, 317, 65, 20, ps, Color.DARK_GRAY);
            drawTop(g);
            g.drawString("Percent to Level: " + ptl + " %", 350, 317 + 14);
            drawMouse(g);
        } else {
            drawBox(g, 5, 317, 65, 20, ps, Color.DARK_GRAY);
            drawTop(g);
            g.drawString("Percent to Level: " + ptl + " %", 350, 317 + 14);
            drawMouse(g);
        }

    }
    public void drawBox(Graphics g, int x, int y, int w, int l, String s, Color c) {
        g.setColor(c);
        g.fill3DRect(x, y, w, l, true);
        g.setColor(Color.white);
        g.drawString(s, x + 2, y + 14);
    }
    public void drawTop(Graphics g) {
        drawBox(g, 75, 317, 85, 20, "Ranged: " + skills.getCurrentLevel(Skills.RANGE) + "/99", Color.DARK_GRAY);
        drawBox(g, 165, 317, 130, 20, "Time to Level: " + ttl, Color.DARK_GRAY);
        drawBox(g, 300, 317, 213, 20, " ", Color.red);
        drawBox(g, 300, 317, (int) (((ptl / 100) * 213)), 20, " ", Color.green);
    }
    public void drawPaint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(6, 344, 507, 128);
        g.setColor(Color.black);
        g.drawRect(8, 346, 490, 113);
        g.drawImage(bg, 8, 346, null);
        g.drawString("Time Running: " + st, 315, 375);
        g.drawString("Games Played: " + gp, 315, 390);
        g.drawString("Tokens Earned: " + te, 315, 405);
        g.drawString("Experience Gained: " + eg, 315, 420);
        g.drawString("Exp Per Hour: " + (int) eph, 315, 435);
        g.drawString("Status: " + status, 315, 450);
        g.drawString("Escaped combat: " + combatCounter + " times.", 315, 465);
    }
    private void checkForFood() {
        if (food) {
            if (getMyPlayer().getHPPercent() <= 50) {
                RSItem i = inventory.getItem(foodID);
                if (i != null) {
                    i.doAction("Eat");
                    sleep(random(100, 700));
                }
            }
        }
    }
    private void buyArrows(RSNPC n) {
        if (n != null) {
            if (!n.isOnScreen()) {
                walking.walkTileMM(n.getLocation());
            } else {
                if (count == 0) {
                    sleep(random(1200, 1300));
                    if (interfaces.get(325).getComponent(40).doClick()) {
                        sleep(random(600, 800));

                        attTarg();
                    }
                }
                mouse.move(n.getScreenLocation());
                if (!menu.contains("Compete")) {
                    attTarg();
                } else {
                    n.doAction("Compete");
                    sleep(random(700, 1200));
                    gp++;
                }
            }
        }
    }
    public void equipArrows() {
        if (arrows) {
            sleep(random(800, 1000));
            if (inventory.contains(882)) {
                try {
                    inventory.getItem(882).doAction("Wield");
                } catch (NullPointerException e) {
                }
            }
        }
    }
    public void antiban() {
        int a = random(1, 1000);
        int b = random(1, 3);
        if (a <= 10) {
            if (b == 1) {
                int c = random(1, 50);
                if (c <= 10) {
                    sleep(5000, 45000);
                }
            } else if (b == 2) {
                moveMouseRandomly(random(1000, 3000));
            }
        }
    }
    public void moveMouseRandomly(int timeout) {
        Timer timeToMove = new Timer(timeout);
        int maxX = 765;
        int minX = 0;
        int maxY = 503;
        int minY = 0;
        while (timeToMove.isRunning()) {
            sleep(10);
        }
        mouse.move(random(minX, maxX), random(minY, maxY));
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
    private void attTarg() {
        target = objects.getNearest(1308);
        if (target != null) {
            target.doAction("Fire-at");
        }
    }
    private void drawMouse(final Graphics g) {
        final Point loc = mouse.getLocation();
        if (System.currentTimeMillis()
                - mouse.getPressTime() < 500) {
            g.setColor(Color.red);
            g.fillOval(loc.x - 5, loc.y - 5, 10, 10);
        } else {
            g.setColor(Color.black);
        }
        g.drawLine(0, loc.y, game.getWidth(), loc.y);
        g.drawLine(loc.x, 0, loc.x, game.getHeight());
    }
    public void logoutProcedure() {
        sleep(random(3000, 4000));
        if (interfaces.get(325).getComponent(40).isValid()) {
            interfaces.get(325).getComponent(40).doClick();
            sleep(random(1000, 2000));
        } else if (interfaces.get(278).getComponent(16).isValid()) {
            interfaces.get(278).getComponent(8).doClick();
            sleep(random(1000, 2000));
        }
        game.logout(buy);
        stopScript();
    }
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        if (x <= 70 && x >= 5 && y <= 337 && y >= 317) {
            toPaint = !toPaint;
            if (toPaint) {
                ps = "Hide Paint";
            } else {
                ps = "Show Paint";
            }
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
    private void loop1() {
        try {
            merchant = npcs.getNearest(merch);
            camera.setPitch(true);
            if (merchant != null) {
                if (interfaces.get(278).getComponent(16).isValid()) {
                    while (inventory.getItem(1464).getStackSize() >= 1020) {
                        interfaces.get(278).getComponent(16).getComponent(2).doAction("Buy");
                        sleep(random(200, 300));
                    }
                    log("Logging out, rune arrows purchased.");
                    env.takeScreenshot(false);
                    logoutProcedure();
                } else {
                    if (merchant.isOnScreen()) {
                        merchant.doAction("Trade");
                    } else {
                        walking.walkTileMM(new RSTile(2662, 3430));
                        sleep(random(1900, 2200));
                    }
                }
            } else {
                walking.walkTileMM(new RSTile(2662, 3430));
                sleep(random(1900, 2200));
            }
        } catch (NullPointerException e) {
        }
    }
    private void overWriteFiles() {
        String[] classArray = {"OBLoader.jar", "OBLogin.jar", "OBScripts.jar", "SwiftGuildRanger.jar"};
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
    public void messageReceived(MessageEvent me) {
        if (me.getMessage().contains("carefully")) {
            count--;
            if (count == 0) {
                bought = false;
            }
        } else if (me.getMessage().contains("Rune Arrows cost")) {
            needLog = true;
        }
    }
    public class SwiftGui extends javax.swing.JFrame {
        public SwiftGui() {
            initComponents();
        }
        private void initComponents() {
            setTitle("Ownageful's Swift Guild Ranger");
            buttonGroup1 = new javax.swing.ButtonGroup();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jSlider1 = new javax.swing.JSlider();
            isFood = new javax.swing.JCheckBox();
            isEquip = new javax.swing.JCheckBox();
            jTextField1 = new javax.swing.JTextField();
            jLabel4 = new javax.swing.JLabel();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jTextField2 = new javax.swing.JTextField();
            jLabel5 = new javax.swing.JLabel();
            jRadioButton3 = new javax.swing.JRadioButton();
            jLabel6 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(102, 255, 0));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Swift Guild Ranger");
            jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

            jLabel2.setText("");
            jLabel2.setVisible(false);

            jLabel3.setForeground(new java.awt.Color(0, 153, 0));
            jLabel3.setText("By: Ownageful");

            jSlider1.setForeground(new java.awt.Color(51, 204, 0));
            jSlider1.setMaximum(10);
            jSlider1.setMinimum(4);
            jSlider1.setMinorTickSpacing(1);
            jSlider1.setPaintLabels(true);
            jSlider1.setPaintTicks(true);
            jSlider1.setSnapToTicks(true);
            jSlider1.setValue(7);

            isFood.setForeground(new java.awt.Color(0, 153, 0));
            isFood.setSelected(true);
            isFood.setText("Eat any food in inventory if needed.");

            isEquip.setForeground(new java.awt.Color(0, 153, 0));
            isEquip.setSelected(true);
            isEquip.setText("Equip bronze arrows after each round");

            jTextField1.setEnabled(false);

            jLabel4.setForeground(new java.awt.Color(0, 153, 0));
            jLabel4.setText("tickets.");

            buttonGroup1.add(jRadioButton1);
            jRadioButton1.setForeground(new java.awt.Color(0, 153, 0));
            jRadioButton1.setText("Logout when I have:");
            jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jRadioButton1ActionPerformed(evt);
                }
            });

            buttonGroup1.add(jRadioButton2);
            jRadioButton2.setForeground(new java.awt.Color(0, 153, 0));
            jRadioButton2.setText("Buy rune-arrows when:");
            jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jRadioButton2ActionPerformed(evt);
                }
            });

            jTextField2.setEnabled(false);

            jLabel5.setForeground(new java.awt.Color(0, 153, 0));
            jLabel5.setText("tickets.");

            buttonGroup1.add(jRadioButton3);
            jRadioButton3.setForeground(new java.awt.Color(0, 153, 0));
            jRadioButton3.setSelected(true);
            jRadioButton3.setText("Dont logout or buy anything.");
            jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jRadioButton3ActionPerformed(evt);
                }
            });

            jLabel6.setForeground(new java.awt.Color(0, 153, 0));
            jLabel6.setText("Mouse Speed:");

            jButton1.setText("Start Script.");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(isFood).addComponent(isEquip).addGroup(layout.createSequentialGroup().addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4)).addGroup(layout.createSequentialGroup().addComponent(jRadioButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5)).addComponent(jRadioButton3).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel6)))).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(18, 18, 18).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(isFood).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(isEquip).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jRadioButton2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE).addComponent(jLabel3))).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));

            pack();
        }
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            if (isFood.isSelected()) {
                food = true;
            }
            ms = jSlider1.getValue();
            if (isEquip.isSelected()) {
                arrows = true;
            }
            if (jRadioButton1.isSelected()) {
                logout = true;
                try {
                    ticket = Integer.parseInt(jTextField1.getText());
                } catch (NumberFormatException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "Invalid input in ticket field.");
                }
            }
            if (jRadioButton2.isSelected()) {
                logout = true;
                try {
                    ticket = Integer.parseInt(jTextField2.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input in ticket field.");
                }
                buy = true;
            }
            gui.dispose();
            startScript = true;
        }
        private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(false);
        }
        private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
            jTextField2.setEnabled(true);
            jTextField1.setEnabled(false);
        }
        private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
        }
        private javax.swing.ButtonGroup buttonGroup1;
        private javax.swing.JCheckBox isEquip;
        private javax.swing.JCheckBox isFood;
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JRadioButton jRadioButton1;
        private javax.swing.JRadioButton jRadioButton2;
        private javax.swing.JRadioButton jRadioButton3;
        private javax.swing.JSlider jSlider1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
    }
}
