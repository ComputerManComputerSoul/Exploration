package view;

import java.awt.*;
import javax.swing.*;

public class TextDialog extends JDialog {  
	//TextDialog类:弹出的文字窗口
	
	private static final long serialVersionUID = 1L;
	public TextDialog(JFrame frame,String title,int width,int height,String text){
		super(frame, title, true);
		setSize(width, height);
		setLocationRelativeTo(null);
		Container container = getContentPane(); // 创建一个容器
		
		JLabel jl=new JLabel(text);
		jl.setFont(new Font("宋体", Font.BOLD, 20));
		jl.setForeground(Color.BLACK);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl); // 在容器中添加标签
		
		container.setBackground (Color.white);
		setVisible(true);	
	}
	
	public TextDialog(JFrame frame,String title,int width,int height,String text,boolean visible){
		super(frame, title, true);
		setSize(width, height);
		setLocationRelativeTo(null);
		Container container = getContentPane(); // 创建一个容器
		
		JLabel jl=new JLabel(text);
		jl.setFont(new Font("宋体", Font.BOLD, 20));
		jl.setForeground(Color.BLACK);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl); // 在容器中添加标签
		
		container.setBackground (Color.white);
		setVisible(visible);
	}
}