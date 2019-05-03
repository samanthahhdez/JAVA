/*
    Window is the class that contains the options menu.
    Also it initializes ImgArea.
    
    Copyright (C) 2019 Samantha Hernández Hernández

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ForkJoinPool;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;


// CLASS WINDOW EXTENDS JFRAME FOR THE GRAPHIC PART AND HAS AN ACTION LISTEN BECAUSE IS THE CLASS THAT THE USER HAS INTERACTION WITH
class  Window extends JFrame implements ActionListener{

// DECLARATION OF AL THE MENU ELEMENTS, IMGAREA CLASS AND FILTERIMAGE CLASS
 ImgArea ia;
 ImgArea.FilterImage ia1;
 JFileChooser chooser; 
 JMenuBar ppalmenu;
 JMenu filemenu;
 JMenu filtermenu2;
 JMenu editmenu;
 JMenu filtermenu;
 JMenuItem submenu_open;
 JMenuItem submenu_save_as;
 JMenuItem submenu_save;
 JMenuItem submenu_exit; 
 JMenuItem submenu_bright; 
 JMenuItem submenu_compress; 
 JMenuItem submenu_resize;
 JMenuItem submenu_rotate;
 JMenuItem submenu_add_text;
 JMenuItem submenu_cancel;
 JMenuItem submenu_filter1;
 JMenuItem submenu_filter2;
 JMenuItem submenu_filter3;
 JMenuItem submenu_filter4;
 JMenuItem submenu_filter5;
 JMenuItem submenu_filter6;
 JMenuItem submenu_filter7;
 JMenuItem submenu_filter8;
 JMenuItem submenu_filter9;
 JMenuItem submenu_filter10;
 String filename;
 ForkJoinPool pool;

 
Window(){
	
  // INITIALIZATION OF THE MENU
	
  ia=new ImgArea();
  
  Container cont=getContentPane();
  cont.add(ia,BorderLayout.CENTER );  
  Font font = new Font("Monospaced", Font.BOLD, 36);
  pool = new ForkJoinPool();
  
  ppalmenu=new JMenuBar();
  ppalmenu.setBackground(Color.WHITE);
  ppalmenu.setFont(font);
  
  filemenu=new JMenu("F I L E");
  filemenu.setBackground(Color.WHITE);

  submenu_open=new JMenuItem("O P E N");
  submenu_open.addActionListener(this);
  submenu_open.setBackground(Color.WHITE);

  submenu_save_as=new JMenuItem("S A V E  A S");
  submenu_save_as.addActionListener(this);
  submenu_save_as.setBackground(Color.WHITE);

  submenu_save=new JMenuItem("S A V E");
  submenu_save.addActionListener(this);  
  submenu_save.setBackground(Color.WHITE);
  
  submenu_exit=new JMenuItem("E X I T");
  submenu_exit.addActionListener(this);
  submenu_exit.setBackground(Color.WHITE);
  
  filemenu.add(submenu_open);
  filemenu.add(submenu_save_as);
  filemenu.add(submenu_save);
  filemenu.add(submenu_exit);  

  editmenu=new JMenu("E D I T");

  submenu_bright=new JMenuItem("B R I G H T N E S S");
  submenu_bright.addActionListener(this);
  submenu_bright.setBackground(Color.WHITE);

  submenu_resize=new JMenuItem("R E S I Z E");
  submenu_resize.addActionListener(this);
  submenu_resize.setBackground(Color.WHITE);
 
  submenu_compress=new JMenuItem("C O M P R E S S");
  submenu_compress.addActionListener(this);
  submenu_compress.setBackground(Color.WHITE);

  submenu_rotate=new JMenuItem("R O T A T E");
  submenu_rotate.addActionListener(this);
  submenu_rotate.setBackground(Color.WHITE);
  
  submenu_cancel=new JMenuItem("C A N C E L");
  submenu_cancel.addActionListener(this);
  submenu_cancel.setBackground(Color.WHITE);

  editmenu.add(submenu_bright);
  editmenu.add(submenu_compress);
  editmenu.add(submenu_resize);
  editmenu.add(submenu_rotate);

  editmenu.add(submenu_cancel);
  
  filtermenu2=new JMenu("R A N D O M  F I L T E R S");
  filtermenu2.setBackground(Color.WHITE);
  
  filtermenu=new JMenu("C O L O R  F I L T E R S");
  filtermenu.setBackground(Color.WHITE);
  
  submenu_filter1=new JMenuItem("B L A C K  &  W H I T E");
  submenu_filter1.addActionListener(this);  
  submenu_filter1.setBackground(Color.WHITE);
  
  submenu_filter2=new JMenuItem("S E P I A");
  submenu_filter2.addActionListener(this);  
  submenu_filter2.setBackground(Color.WHITE);
  
  submenu_filter3=new JMenuItem("P U R P L E");
  submenu_filter3.addActionListener(this);  
  submenu_filter3.setBackground(Color.WHITE);
  
  submenu_filter4=new JMenuItem("B L U E");
  submenu_filter4.addActionListener(this);  
  submenu_filter4.setBackground(Color.WHITE);
  
  submenu_filter5=new JMenuItem("G R E E N");
  submenu_filter5.addActionListener(this);  
  submenu_filter5.setBackground(Color.WHITE);
  
  submenu_filter6=new JMenuItem("B L U R");
  submenu_filter6.addActionListener(this);  
  submenu_filter6.setBackground(Color.WHITE);
  
  submenu_filter7=new JMenuItem("W H I T E B O A R D");
  submenu_filter7.addActionListener(this);  
  submenu_filter7.setBackground(Color.WHITE);
  
  submenu_filter8=new JMenuItem("E D G E S");
  submenu_filter8.addActionListener(this);  
  submenu_filter8.setBackground(Color.WHITE);
  
  submenu_filter9=new JMenuItem("S H A R P E N");
  submenu_filter9.addActionListener(this);  
  submenu_filter9.setBackground(Color.WHITE);
  
  submenu_filter10=new JMenuItem("U G L Y");
  submenu_filter10.addActionListener(this);  
  submenu_filter10.setBackground(Color.WHITE);
  
  filtermenu.add(submenu_filter1);
  filtermenu.add(submenu_filter2);
  filtermenu.add(submenu_filter3);
  filtermenu.add(submenu_filter4);
  filtermenu.add(submenu_filter5);
  filtermenu2.add(submenu_filter6);
  filtermenu2.add(submenu_filter7);
  filtermenu2.add(submenu_filter8);
  filtermenu2.add(submenu_filter9);
  filtermenu2.add(submenu_filter10);

  ppalmenu.add(filemenu);
  ppalmenu.add(editmenu);
  ppalmenu.add(filtermenu);
  ppalmenu.add(filtermenu2);
  setJMenuBar(ppalmenu);
  
  setTitle("I M A G E       E D I T O R  :)");
  setDefaultCloseOperation(EXIT_ON_CLOSE);
  setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
     setVisible(true); 

  // INITIALIZATION OF FILE CHOOSER (FOR OPEN THE IMAGES)   
  
  chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "gif","bmp","png");
      chooser.setFileFilter(filter);
      chooser.setMultiSelectionEnabled(false);
  enableSaving(false);
  ia.requestFocus();
  
}

// METHOD THAT GETS THE CLICK OF THE USER INSIDE A ITEM OF THE MENU

 public void actionPerformed(ActionEvent e){

  JMenuItem source = (JMenuItem)(e.getSource());
  if(source.getText().compareTo("O P E N")==0)  {
    setImage();
    ia.repaint();
    ia.imageLoaded=true;
    validate();
     }
  
  else if(source.getText().compareTo("B L A C K  &  W H I T E")==0) {
	  System.out.println(filename);
	  if (ia.edited) { // IF THE IMAGE IS ALREADY EDITED, WE CONTINUE TO EDIT THAT ONE
	  ia1=ia.new FilterImage(1);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 1); // IF THE IMAGE DOESN´T HAVE ANY FILTER WE NEED TO INITIALIZE IT AS A BUFFERED IMAGE TO START APPLYING FILTERS
	  pool.invoke(ia1); // INVOKES THREADPOOL 
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("S E P I A")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(2);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 2);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("P U R P L E")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(3);
	  pool.invoke(ia1);  // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 3);
	  pool.invoke(ia1);  // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("B L U E")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(4);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 4);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("G R E E N")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(5);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 5);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("B L U R")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(6);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 6);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("W H I T E B O A R D")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(7);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 7);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("E D G E S")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(8);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 8);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("S H A R P E N")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(9);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 9);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  else if(source.getText().compareTo("U G L Y")==0) {
	  System.out.println(filename);
	  if (ia.edited) {
	  ia1=ia.new FilterImage(10);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }else{
	  ia1=ia.new FilterImage(filename, "c1", 10);
	  pool.invoke(ia1); // INVOKES THREADPOOL
	  }
	  ia.edited=true;
	  enableSaving(true); 
  }
  
  
  else if(source.getText().compareTo("S A V E  A S")==0){
	  showSaveFileDialog();     
   }
  
  else if(source.getText().compareTo("S A V E")==0){  
	  ia.saveToFile(filename);  
   }
  
  else if(source.getText().compareTo("B R I G H T N E S S")==0){
	  ImageBrightness ib=new ImageBrightness(); 
	  if(ImgArea.imageLoaded)
	  ib.enableSlider(true);
	  ia.edited=true;
  }
  
  else if(source.getText().compareTo("C O M P R E S S")==0){
	  if(ImgArea.imageLoaded){
	   ia.setActionCompressed(true);
	   enableSaving(true);
	   ia.edited=true;
	   } 
   }

  else if(source.getText().compareTo("R E S I Z E")==0){
	  ImageResize ir=new ImageResize();
	  if(ImgArea.imageLoaded)
	  ir.enableComponents(true);  
	  ia.edited=true;
  }
  
  else if(source.getText().compareTo("R O T A T E")==0){ 
	  if(ImgArea.imageLoaded){
	   ia.rotateImage();
	   ia.edited=true;
	   enableSaving(true);
	   } 
  }

  else if(source.getText().compareTo("C A N C E L")==0) {
	  ia.setImgFileName(filename);
	  ia.reset();
	  ia.edited=false;
  }

  else if(source.getText().compareTo("E X I T")==0) 
	  System.exit(0);    
  } 
      
// SHOWS THE WINDOW WITH THE FILE EXPLORER (CLICK ON "OPEN")
 public void setImage(){
	 int returnVal = chooser.showOpenDialog(this);
     if(returnVal == JFileChooser.APPROVE_OPTION) {   
    	 filename=chooser.getSelectedFile().toString();
    	 ia.prepareImage(filename);
     }      
  }

 // SHOWS THE WINDOW TO SAVE THE IMAGE (CLICK ON SAVE AS)
 public void showSaveFileDialog(){
	 int returnVal = chooser.showSaveDialog(this);
	 if(returnVal == JFileChooser.APPROVE_OPTION) {  
		 String filen=chooser.getSelectedFile().toString(); 
         ia.saveToFile(filen);  
     }
 }
 
 
 // ENABLES THE SAVE AS AND SAVE OPTIONS (ITS ENABLED WHEN THERE'S A PHOTO)
 public void enableSaving(boolean f){
	 submenu_save_as.setEnabled(f);
	 submenu_save.setEnabled(f); 
  }
 
 // THIS CLASS CONTROLS THE BAR THAT APPEARS WHEN THE USER SELECTS BRIGHTNESS..
 public class ImageBrightness extends JFrame implements ChangeListener{
	 JSlider slider;
	 ImageBrightness(){
		 addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent e){
				 dispose(); 
			 }
		 });
	Container cont=getContentPane();  
	slider=new JSlider(-10,10,0); 
	slider.setEnabled(false);
	slider.addChangeListener(this);
	cont.add(slider,BorderLayout.CENTER); 
	slider.setEnabled(true);
	setTitle("I M A G E - B R I G H T N E S S");
	setPreferredSize(new Dimension(300,100));
	setVisible(true);
	pack();
	enableSlider(false);
 }

// METHOD THAT SHOWS THE SLIDER
 public void enableSlider(boolean enabled){
	 slider.setEnabled(enabled);
 }
 
 // WHEN THE USER MOVES THE POSITION OF THE SLIDER, IMGAREA CLASS FILTERIMAGE METHOD IS CALLED AND THE IMAGE IS FILTERED AND REPAINTED
 public void stateChanged(ChangeEvent e){
	 ia.setValue(slider.getValue()/10.0f);
	 ia.setActionSlided(true);   
	 ia.filterImage();
	 ia.repaint();
	 enableSaving(true);
 }
}
 
// CLASS FOR RESIZING AN IMAGE (CONTROLS THE POPUP WINDOW)
public class ImageResize extends JFrame implements ActionListener {
	JPanel panel;
	JTextField txtWidth;
	JTextField txtHeight;
	JButton btOK;
	ImageResize(){
	setTitle("Image resize");
	setPreferredSize(new Dimension(400,100));
	btOK=new JButton("OK");
	btOK.setBackground(Color.WHITE);
	btOK.setForeground(Color.BLACK);  
	btOK.addActionListener(this);
	txtWidth=new JTextField(4);
	txtWidth.addKeyListener((KeyListener) new KeyList());
	txtHeight=new JTextField(4);
	txtHeight.addKeyListener((KeyListener) new KeyList());
	panel=new JPanel();
	panel.setLayout(new FlowLayout());
	panel.add(new JLabel("Width:"));
	panel.add(txtWidth);
	panel.add(new JLabel("Height:"));
	panel.add(txtHeight);
 	panel.add(btOK);
 	panel.setBackground(Color.WHITE);
 	add(panel, BorderLayout.CENTER);
 	setVisible(true);
 	pack();
 	enableComponents(false);
 }

 // METHOD THAT SHOWS THE WINDOW WITH THE INPUTS FOR WIDTH AND HEIGHT
 public void enableComponents(boolean enabled){
  txtWidth.setEnabled(enabled);
  txtHeight.setEnabled(enabled);
  btOK.setEnabled(enabled);
 }
 
// METHOD THAT CHANGES THE IMAGE SIZE WHEN THE USER SELECTS RESIZE OPTION
 public void actionPerformed(ActionEvent e){
	 if(e.getSource()==btOK){
		 ia.setActionResized(true);     
		 ia.resizeImage(Integer.parseInt(txtWidth.getText()),Integer.parseInt(txtHeight.getText())); // CALLS THE IMGAREA RESIZE METHOD
		 enableSaving(true);
		 ia.repaint();
	 }
 }

// METHOD TO READ THE KEY VALUES WHEN CHOOSING A NEW SIZE FOR THE IMAGE RESIZE
 public class KeyList extends KeyAdapter{
	public void keyTyped(KeyEvent ke){
    char c = ke.getKeyChar(); 
    int intkey=(int)c;
    if(!(intkey>=48 && intkey<=57 || intkey==8 || intkey==127)){
    	ke.consume(); 
    }  
	}  
}

}
}
