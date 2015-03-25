package com.blaster;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import com.blaster.data.OreData;

@SuppressWarnings("serial")
public class BlasterGUI extends JFrame {
	public boolean isReady;
	private DreamBlaster ctx;
	
	public BlasterGUI(DreamBlaster... ctx) {
		this.ctx = ctx[0];
		initComponents();
	}

	private void started(ActionEvent e) {
		setVisible(false);
		isReady = true;
		ctx.setPedal(usePedals.isSelected());
		ctx.setPipes(fixPipes.isSelected());
		ctx.setPump(usePump.isSelected());
		ctx.setBag(useCoalBag.isSelected());
		ctx.log("---------- ready ----------");
	}

	private void initComponents() {
		setTitle("Script settings");
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400, 320);
		final Container cp = getContentPane();
		cp.setLayout(null);

		int x = 40;
		int y = 30;

		selectedOre.setModel(new DefaultComboBoxModel<>(OreData.values()));
		selectedOre.setBounds(x, y, 100, 20);
		selectedOre.setSelectedIndex(0);
		selectedOre.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item instanceof OreData) {
						ctx.setChosenOre((OreData) item);
						ctx.log("/ My ore set to [" + ctx.getChosenOre().getName() + "]");
						cp.repaint();
					}
				}
			}
		});

		y += 40;

		usePedals.setBounds(x, y, 150, 20);
		usePedals.setText("Use pedals (30 agility)");

		y += 30;

		fixPipes.setBounds(x, y, 150, 20);
		fixPipes.setText("Use pipes (30 crafting)");

		y += 30;

		usePump.setBounds(x, y, 100, 20);
		usePump.setText("Use pump (30 strength)");

		y += 30;

		useCoalBag.setBounds(x, y, 100, 20);
		useCoalBag.setText("Use pump (30 strength)");

		y += 40;

		cp.add(selectedOre);
		cp.add(usePedals);
		cp.add(fixPipes);
		cp.add(usePump);
		cp.add(useCoalBag);
		cp.add(startButton);

		startButton.setText("Start");
		startButton.setBounds(3, getHeight() - 60, getWidth() - 6,
				startButton.getPreferredSize().height);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				started(e);
			}
		});
	}
	
	private JComboBox<OreData> selectedOre = new JComboBox<OreData>();
	private JCheckBox usePedals = new JCheckBox();
	private JCheckBox fixPipes = new JCheckBox();
	private JCheckBox usePump = new JCheckBox();
	private JCheckBox useCoalBag = new JCheckBox();
	private JButton startButton = new JButton();
}