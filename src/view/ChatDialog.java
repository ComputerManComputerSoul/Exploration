package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChatDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private String chatMessages;
	private GameFrame gameFrame;
	private JLabel label = new JLabel();
	private JScrollPane scrollPane;
	private JTextField textField;
	private JButton handInButton;
	private Container container = getContentPane();
	
	public ChatDialog(GameFrame gameFrame){
		super(gameFrame,"����", true);
		chatMessages="";
		this.gameFrame = gameFrame;
		
		setLayout(null);
		setSize(600, 400);
		setLocationRelativeTo(null);
        setBackground(Color.white);
        setResizable(false);
		
		label = new JLabel();
		label.setFont(new Font("����", Font.BOLD, 20));
		label.setForeground(Color.BLACK);
		label.setBackground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		
		scrollPane = new JScrollPane(label);
		scrollPane.setForeground(Color.black);
		scrollPane.setBackground(Color.white);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);	//����������
		scrollPane.setBounds(0,0,587,323);
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(false);
		container.add(scrollPane); // ����������ӱ�ǩ
        
		textField = new JTextField("",20);
		textField.setFont(new Font("����", Font.BOLD, 18));
		textField.setForeground(Color.black);
		textField.setBackground(Color.white);
		textField.setBounds(1, 323, 485, 40);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageToSend=textField.getText();
				if(messageToSend.equals("")) {
					textField.requestFocus();
					return;
				}
				addMessage(messageToSend,true);
				gameFrame.getMyData().sendMessage("9,"+messageToSend);
				textField.setText("");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				textField.requestFocus();
			}
		});
        container.add(textField);
        
        handInButton = new JButton("����");
        handInButton.setFont(new Font("����", Font.BOLD, 18));
        handInButton.setForeground(Color.black);
        handInButton.setBackground(Color.white);
        handInButton.setFocusPainted(false);
        handInButton.setBounds(485,323,100,39);
        handInButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			public void actionPerformed(ActionEvent e) {
				String messageToSend=textField.getText();
				if(messageToSend.equals("")) {
					textField.requestFocus();
					return;
				}
				addMessage(messageToSend,true);
				gameFrame.getMyData().sendMessage("9,"+messageToSend);
				textField.setText("");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				textField.requestFocus();
			}
		});
		container.add(handInButton);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	//������Ϣ
	public void addMessage(String message,boolean senderIsMe) {
		String handledMessage=(senderIsMe?"��:":"����:")+message;
		chatMessages+=handledMessage;
		chatMessages+="<br/><br/>";
		label.setText("<html>"+chatMessages+"</html>");
		if(!senderIsMe) gameFrame.refreshChatMessageText(handledMessage);
	}
}
