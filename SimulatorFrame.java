/*
 * Copyright Daniel Norman 2014
 */
package com.DanielNorman.ClothSimulator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SimulatorFrame extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
	private static GraphicsPanel graphicsPanel;
	private int mouseButton = MouseEvent.NOBUTTON;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorFrame frame = new SimulatorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	


	/**
	 * Create the frame.
	 */
	public SimulatorFrame() {
		setTitle("Cloth Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 645, 488);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) { graphicsPanel.canRun = false; }
			@Override
			public void windowClosing(WindowEvent arg0) { graphicsPanel.canRun = false; }
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
		graphicsPanel = new GraphicsPanel();
		graphicsPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
			graphicsPanel.handleMouseDrag(mouseButton, e.getX(), e.getY(), e.getModifiers());
			}
		});
		graphicsPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseButton = e.getButton();
				graphicsPanel.handleMouseClick(mouseButton, e.getX(), e.getY(), e.getModifiers());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseButton = MouseEvent.NOBUTTON;
				graphicsPanel.handleMouseRelease(mouseButton, e.getX(), e.getY(), e.getModifiers());
			}
		});
		graphicsPanel.setBounds(0, 0, 400, 440);
		contentPane.add(graphicsPanel);
		graphicsPanel.setLayout(null);
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graphicsPanel.shouldReset = true;
			}
		});
		resetButton.setBounds(468, 117, 89, 23);
		contentPane.add(resetButton);
		
		final JCheckBox softGrabCheckBox = new JCheckBox("Soft grab");
		softGrabCheckBox.setSelected(true);
		softGrabCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graphicsPanel.softGrab = softGrabCheckBox.isSelected();
			}
		});
		softGrabCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		softGrabCheckBox.setBounds(421, 165, 97, 23);
		contentPane.add(softGrabCheckBox);
		
		final JCheckBox colorCodedCheckBox = new JCheckBox("Color coded");
		colorCodedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.colorCoded = colorCodedCheckBox.isSelected();
			}
		});
		colorCodedCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		colorCodedCheckBox.setBounds(421, 217, 109, 23);
		contentPane.add(colorCodedCheckBox);
		
		JLabel lblGravity = new JLabel("Gravity:");
		lblGravity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGravity.setBounds(410, 295, 71, 23);
		contentPane.add(lblGravity);
		
		final JSlider gravitySlider = new JSlider();
		gravitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				graphicsPanel.gravity = gravitySlider.getValue() / 1000.0;
				graphicsPanel.shouldUpdateSettings = true;
			}
		});
		gravitySlider.setValue(2);
		gravitySlider.setMaximum(5);
		gravitySlider.setMajorTickSpacing(1);
		gravitySlider.setPaintTicks(true);
		gravitySlider.setBounds(512, 295, 109, 31);
		contentPane.add(gravitySlider);
		
		final JSlider stiffnessSlider = new JSlider();
		stiffnessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				graphicsPanel.stiffness = stiffnessSlider.getValue() / 10.0;
				graphicsPanel.shouldUpdateSettings = true;
			}
		});
		stiffnessSlider.setMinimum(1);
		stiffnessSlider.setValue(5);
		stiffnessSlider.setPaintTicks(true);
		stiffnessSlider.setMaximum(7);
		stiffnessSlider.setMajorTickSpacing(1);
		stiffnessSlider.setBounds(512, 337, 109, 31);
		contentPane.add(stiffnessSlider);
		
		JLabel lblStiffness = new JLabel("Stiffness:");
		lblStiffness.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStiffness.setBounds(410, 337, 71, 23);
		contentPane.add(lblStiffness);
		
		JLabel lblTearDistance_1 = new JLabel("Tear distance:");
		lblTearDistance_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTearDistance_1.setBounds(410, 379, 93, 23);
		contentPane.add(lblTearDistance_1);
		
		final JSlider tearDistanceSlider = new JSlider();
		tearDistanceSlider.setValue(16);
		tearDistanceSlider.setPaintTicks(true);
		tearDistanceSlider.setMinimum(2);
		tearDistanceSlider.setMaximum(20);
		tearDistanceSlider.setMajorTickSpacing(2);
		tearDistanceSlider.setBounds(512, 379, 109, 31);
		tearDistanceSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				graphicsPanel.tearDistance = tearDistanceSlider.getValue() * graphicsPanel.pointDistance;
				if (tearDistanceSlider.getValue() == tearDistanceSlider.getModel().getMaximum()) graphicsPanel.tearDistance = Integer.MAX_VALUE;
				graphicsPanel.shouldUpdateSettings = true;
			}
		});
		contentPane.add(tearDistanceSlider);
		
		JLabel lblPointDensity = new JLabel("Point distance:");
		lblPointDensity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPointDensity.setBounds(410, 261, 93, 23);
		contentPane.add(lblPointDensity);
		
		final JSlider pointDensitySlider = new JSlider();
		pointDensitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				graphicsPanel.pointDistance = pointDensitySlider.getValue();
				graphicsPanel.shouldReset = true;
			}
		});
		pointDensitySlider.setMinimum(10);
		pointDensitySlider.setValue(14);
		pointDensitySlider.setPaintTicks(true);
		pointDensitySlider.setMaximum(24);
		pointDensitySlider.setMajorTickSpacing(2);
		pointDensitySlider.setBounds(512, 261, 109, 31);
		contentPane.add(pointDensitySlider);
		
		JLabel lblNewLabel = new JLabel("Left-click to grab the cloth.");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(418, 32, 211, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblRightclickToCut = new JLabel("Right-click to cut the cloth.");
		lblRightclickToCut.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRightclickToCut.setBounds(418, 66, 211, 23);
		contentPane.add(lblRightclickToCut);
		
		final JCheckBox fillClothCheckBox = new JCheckBox("Fill cloth");
		fillClothCheckBox.setSelected(true);
		fillClothCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graphicsPanel.fillCloth = fillClothCheckBox.isSelected();
				colorCodedCheckBox.setEnabled(fillClothCheckBox.isSelected());
			}
		});
		fillClothCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fillClothCheckBox.setBounds(421, 191, 109, 23);
		contentPane.add(fillClothCheckBox);
	}
}
