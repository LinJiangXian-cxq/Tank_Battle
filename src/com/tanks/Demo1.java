//�Լ���̹�˿ɿ����ٶ��ƶ�
//����̨�з�̹�˳���
//����̹���ܷ��ӵ����ӵ�����
//�������ез�������
//�з�������ƶ��������ӵ�
//�з�������̹�˻��Χ����  
//�з�������Ҫ��ը
//�ҷ�������Ҫ��ը
//
//al:�з�̹�˼�           als���з��ӵ���         alm���ҷ��ӵ�
package com.tanks;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.*;
import java.math.*;
public class Demo1 extends JFrame implements ActionListener{
	Mypanel mp=null;
	Mystartpanel msp=null;
	//������˸Ч���Ĳ���
	int time=0;
	JMenuBar jmb=null;
	JMenu jm=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		Demo1 d=new Demo1();
	}
	public Demo1()
	{
		
		mp=new Mypanel();
		msp=new Mystartpanel();
		Thread tmsp=new Thread(msp);
		tmsp.start();
		
		jmb=new JMenuBar();
		this.setJMenuBar(jmb);
		
		jm=new JMenu("��Ϸ�˵�");
		jm.setMnemonic('G');
		jmb.add(jm);
		
		jmi1=new JMenuItem("��ʼ��Ϸ");
		jm.add(jmi1);
		jmi1.addActionListener(this);
		jmi1.setActionCommand("start");
		
		jmi2=new JMenuItem("������Ϸ");
		jm.add(jmi2);
		
		this.add(msp);
		//���ô�������
		this.setTitle("̹�˴�ս֮");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	class  Mystartpanel extends JPanel implements Runnable
	{
		public void paint(Graphics g)
		{
			super.paint(g);
			g.fillRect(0, 0, 400, 300);
			if(time%2==0)
			{
				g.setColor(Color.RED);
				Font myfont=new Font("΢���ź�",0, 30);
				g.setFont(myfont);
				g.drawString("��һ��", 150, 150);
			}			
		}

		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			while(true)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				time++;
				this.repaint();
			}
		}
	}
	class Mypanel extends JPanel implements KeyListener,Runnable
	{
		//����
//		Image image=null;
		Mytank t=null;
		Animytanks et;
		//�����з�̹�˼���
		ArrayList<Animytanks> al;
		ArrayList<Boom> aboom=new ArrayList<Boom>();
		Record record=new Record();
		//���캯��
		public Mypanel()
		{
			//��ʼ���ҷ�̹��λ�ã�x��y�������ٶ�
			t=new Mytank(180, 270,0,5);
			//��ʼ���з�̹�������Լ�λ��{̹�˼���}
			al=new ArrayList<Animytanks>();
			int e=3;
			for(int i=0;i<e;i++)
			{
				Animytanks et=null;
				et=new Animytanks((i+1)*90,0,1,5);
				//�ѵз�̹�˼�����ӵ�  al����Animytanks����istouched�����������õ�
				et.setanimy(al);
				//�����з�̹���ƶ����̣߳�ֵ��ע����ǣ�ÿһ���з�̹�˶�Ҫ����һ���̣߳�
				Thread t4=new Thread(et);
				t4.start();
				//��ӵ��з�̹�˵�����
				al.add(et);
			}
		}
		//������Ϣ����
		public void p(Graphics g)
		{
			g.setColor(Color.cyan);
			this.drawmy(50, 330, g, 0, 0);
			g.drawString(record.getMtnum()+"", 80, 350);
			
			g.setColor(Color.red);
			this.drawmy(100, 330, g, 0, 0);
			g.drawString(record.getEtnum()+"",130 , 350);
			
			
		}
		public void paint(Graphics g)
		{
			super.paint(g);
			//�������Χ
			g.fillRect(0, 0, 420, 320);
			
			//��ɱ���
			Font myfont=new Font("΢���ź�",0, 30);
			g.drawString("�ҵ��ܳɼ�", 450, 50);
			g.setColor(Color.red);
			this.drawmy(450, 75, g, 0, 0);
			g.drawString(record.getHtnum()+"", 480, 95);
			
			//������Ϣ
			this.p(g);
			
			//���ҷ�̹��
			if(t.isLive==true)
			{
				g.setColor(Color.cyan);
				this.drawmy(t.getX(),t.getY(), g, t.getChaoxiang(),t.getSpeed());
			}
			this.repaint();
			//�������ӵ�
			for(int j=0;j<t.alm.size();j++)
			{
				t.ss=(Myshot)t.alm.get(j);
				if(t.ss!=null&&t.ss.isLive==true)
				{
					g.fill3DRect(t.ss.x, t.ss.y, 2, 2, false);
				}
				//ɾ�������ӵ�
				if(t.ss!=null&&t.ss.isLive==false)
				{
					t.alm.remove(t.ss);
				}
			}	
			//���з�̹��
			for(int i=0;i<al.size();i++)
			{
				Myshot enemyshot=null;
				g.setColor(Color.red);
				et=(Animytanks)al.get(i);
				//ֻ�л��ŲŻ���̹��
				if(et.isLive==true)
				{
					this.drawmy(et.getX(), et.getY(), g, et.getChaoxiang(), et.getSpeed());
					//�����з��ӵ�
					for(int j=0;j<et.als.size();j++)
					{
						//ȡ���ӵ�
						enemyshot=et.als.get(j);
						//�ӵ����
						if(enemyshot.isLive==true)
						{
							if(enemyshot.isLive)
							{
								g.draw3DRect(enemyshot.x, enemyshot.y, 2, 2, false);
							}
						}
						//�ӵ�����
						else
						{
							et.als.remove(enemyshot);
						}
					}
				}
				//����̹��ɾ��
				if(et.isLive==false)
				{
					al.remove(et);
				}
			}
			this.repaint();
			for(int i=0;i<aboom.size();i++)
			{
				
				Boom boom=aboom.get(i);
				if(boom!=null)
				{
					g.setColor(Color.pink);
//					g.drawOval(boom.x, boom.y, 35, 35);
//					Image im=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/mytank and animy tanks/src/com/tanks/1.png"));
//					Image im1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/mytank and animy tanks/src/com/tanks/boom.png"));
//			        g.drawImage(im, boom.x, boom.y, 30, 30, this); 
//			        g.drawImage(im1, boom.x, boom.y, 30, 30, this); 
					Image im1=Toolkit.getDefaultToolkit().getImage("boom.png");
					if(boom.time!=0)
					{
						g.drawImage(im1, boom.x, boom.y, 30, 30, this);
						boom.time--;
						System.out.println(boom.time);
					}
					if(boom.time==0)
					{
						aboom.remove(boom);
					}
				}			
				
			}
		}
		public void drawmy(int x,int y,Graphics g,int chaoxiang,float speed)
		{
			//�����ϡ��� ��������̹����̬
			if(chaoxiang==0)
			{
				g.fill3DRect(x, y, 5, 30, false);
				g.fill3DRect(x+15, y, 5, 30, false);
				g.fill3DRect(x+5, y+5, 10, 20, false);
				g.fillOval(x+5, y+10, 10, 10);
				g.drawLine(x+10, y+15, x+10, y);
			}
			if(chaoxiang==1)
			{
				g.fill3DRect(x, y, 5, 30, false);
				g.fill3DRect(x+15, y, 5, 30, false);
				g.fill3DRect(x+5, y+5, 10, 20, false);
				g.fillOval(x+5, y+10, 10, 10);
				g.drawLine(x+10, y+15, x+10, y+29);
			}
			if(chaoxiang==2)
			{
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5, y+5, 20, 10, false);
				g.fillOval(x+10, y+5, 10, 10);
				g.drawLine(x+15, y+10, x, y+10);
			}
			if(chaoxiang==3)
			{
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5, y+5, 20, 10, false);
				g.fillOval(x+10, y+5, 10, 10);
				g.drawLine(x+15, y+10, x+29, y+10);
			}

		}
		public void hitme(Myshot ms,Mytank t)
		{
			//���³���
			if(t.chaoxiang==0||t.chaoxiang==1)
			{
				//����
				if(ms.x>t.x&&ms.x<t.x+20&&ms.y>t.y&&ms.y<t.y+30)
				{
					//�ӵ�����
					ms.isLive=false;
					//�з�����
					t.isLive=false;
					record.hmt();
					Boom boom=new Boom(t.x,t.y);
					aboom.add(boom);
				}
			}
			//���ҳ���
			if(t.chaoxiang==2||t.chaoxiang==3)
			{
				if(ms.x>t.x&&ms.x<t.x+30&&ms.y>t.y&&ms.y<t.y+20)
				{
					//�ӵ�����
					ms.isLive=false;
					//�з�����
					t.isLive=false;
					record.hmt();
					Boom boom=new Boom(t.x,t.y);
					aboom.add(boom);
				}
			}
		}
		//�ҷ��ӵ��Ƿ���ез�̹��
		public void hitanimy(Myshot ss,Animytanks et)
		{
			//�з����³���
			if(et.chaoxiang==0||et.chaoxiang==1)
			{
				//����
				if(ss.x>et.x&&ss.x<et.x+20&&ss.y>et.y&&ss.y<et.y+30)
				{
					//�ӵ�����
					ss.isLive=false;
					//�з�����
					et.isLive=false;
					record.het();
					record.heihei();
					Boom boom=new Boom(et.x,et.y);
					aboom.add(boom);
				}
			}
			//�з����ҳ���
			if(et.chaoxiang==2||et.chaoxiang==3)
			{
				if(ss.x>et.x&&ss.x<et.x+30&&ss.y>et.y&&ss.y<et.y+20)
				{
					//�ӵ�����
					ss.isLive=false;
					//�з�����
					et.isLive=false;
					record.het();
					record.heihei();
					Boom boom=new Boom(et.x,et.y);
					aboom.add(boom);
				}
			}
		}

		//���������ӿ�ʵ��
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO �Զ����ɵķ������
			if(arg0.getKeyCode()==38)
			{
				//�ƶ�
				if(t.y>0)
				{
					t.shang();
					//̹�˳���
					t.setChaoxiang(0);
				}
				
			}
			if(arg0.getKeyCode()==40)
			{
				if(t.y<300)
				{
					t.xia();
					t.setChaoxiang(1);
				}
			}
			if(arg0.getKeyCode()==37)
			{
				if(t.x>0)
				{
					t.zuo();
					t.setChaoxiang(2);
				}
			}
			if(arg0.getKeyCode()==39)
			{
				if(t.x<400)
				{
					t.you();
					t.setChaoxiang(3);
				}
			}
			//j������ʱִ��shotanimy�����������ӵ��ƶ��߳�
			if(arg0.getKeyCode()==74)
			{
				//��shotanimy������ʼ�ӵ�      �������ӵ��ƶ��߳�
				if(t.alm.size()<=5)
				{
					t.shotanimy();
				}
			}
			
		}
		

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO �Զ����ɵķ������
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO �Զ����ɵķ������
			
		}



		@Override
		//����repaint�߳�            ����Mypanel�Ĺ��캯�����������߳�
		public void run() {
			// TODO �Զ����ɵķ������
			while(true)
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				//�ҷ��ӵ����ез���ķ���
				for(int i=0;i<t.alm.size();i++)
				{
					Myshot myshot=null;
					myshot=(Myshot)t.alm.get(i);
					if(myshot.isLive==true)
					{
						for(int j=0;j<al.size();j++)
						{
							Animytanks animytanks=null;
							animytanks=(Animytanks)al.get(j);
							if(animytanks.isLive==true)
							{
								this.hitanimy(myshot, animytanks);
							}
						}
					}
				}
				//���Ƶз��Ƿ�������ӵ�
				for(int i=0;i<al.size();i++)
				{
					Animytanks et=al.get(i);
					if(et.isLive)
					{
						//�涨ÿ��̹���ӵ���С��3
						if(et.als.size()<3)
						{

							Myshot s=null;
							if(et.chaoxiang==0)
							{
								s=new Myshot(et.x+10,et.y-1,2,et.chaoxiang);
								et.als.add(s);
							}
							if(et.chaoxiang==1)
							{
								s=new Myshot(et.x+10,et.y+30,2,et.chaoxiang);
								et.als.add(s);
							}
							if(et.chaoxiang==2)
							{
								s=new Myshot(et.x-1,et.y+10,2,et.chaoxiang);
								et.als.add(s);
							}
							if(et.chaoxiang==3)
							{
								s=new Myshot(et.x+30,et.y+10,2,et.chaoxiang);
								et.als.add(s);
							}
							//�ӵ��ƶ��߳�
							Thread t7=new Thread(s);
							t7.start();
						}
					}
				}
				
				//�з��ӵ��Ƿ�����ҷ�
				for(int i=0;i<al.size();i++)
				{
					Animytanks animytanks=(Animytanks)al.get(i);
					if(animytanks.isLive==true)
					{
						for(int j=0;j<animytanks.als.size();j++)
						{
							Myshot animyshot=(Myshot)animytanks.als.get(j);
							if(animyshot.isLive==true)
							{
								this.hitme(animyshot,t);
							}
							
						}
					}
				}
				
				this.repaint();
			}
		}
	}
	
	class Record
	{
		private int etnum=20;
		private int mtnum=3;
		private int htnum=0;
		public int getEtnum() {
			return etnum;
		}
		public void setEtnum(int etnum) {
			this.etnum = etnum;
		}
		public int getMtnum() {
			return mtnum;
		}
		public void setMtnum(int mtnum) {
			this.mtnum = mtnum;
		}
		public int getHtnum() {
			return htnum;
		}
		public void setHtnum(int htnum) {
			this.htnum = htnum;
		}
		public void het()
		{
			etnum--;
		}
		public void hmt()
		{
			mtnum--;
		}
		public void heihei()
		{
			htnum++;
		}
	}
	
	class Boom
	{
		int x;
		int y;
		int time=15;
		public Boom(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
	}


	class Shot
	{
		int x=0;
		int y=0;
		int speed=5;
		int chaoxiang=0;
		public Shot(int x,int y,int z,int chaoxiang)
		{
			this.x=x;
			this.y=y;
			this.speed=z;
			this.chaoxiang=chaoxiang;
			
		}
	}
	class Myshot extends Shot implements Runnable
	{
		Mytank t=null;
		boolean isLive=true;
		public Myshot(int x, int y, int z, int chaoxiang) {
			super(x, y, z, chaoxiang);
			// TODO �Զ����ɵĹ��캯�����
		}
		public void run()
		{
			while(true)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				if(this.chaoxiang==0)
				{
					y=y-speed;
				}
				if(this.chaoxiang==1)
				{
					y=y+speed;
				}
				if(this.chaoxiang==2)
				{
					x=x-speed;
				}
				if(this.chaoxiang==3)
				{
					x=x+speed;
				}
				//�ж��ӵ��Ƿ����
				if(x<0||x>400||y<0||y>300)
				{
					this.isLive=false;
				}
//				if(this.isLive==false)
//				{
//					break;
//				}
			}
		}
	}
	class Tank
	{
		int x;
		int y;
		int chaoxiang;
		int speed;
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public int getChaoxiang() {
			return chaoxiang;
		}
		public void setChaoxiang(int chaoxiang) {
			this.chaoxiang = chaoxiang;
		}
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
		}
		public Tank(int x,int y,int z,int s)
		{
			this.x=x;
			this.y=y;
			this.chaoxiang=z;
			this.speed=s;
		}
	}
	class Mytank extends Tank
	{
		boolean isLive=true;
		Myshot ss=null;
		ArrayList<Myshot> alm=new ArrayList<Myshot>();
		public Mytank(int x, int y, int z, int s) {
			super(x, y, z, s);
		}
		//�ҷ�̹�˵��ӵ�����     ���ҷ�̹�˵���������ϣ�x��y�� ������ʼ���ӵ�    �˷�������j���������õ�
		public void shotanimy()
		{
			if(chaoxiang==0)
			{
				ss=new Myshot(x+10,y-1,5,chaoxiang);
				alm.add(ss);
			}
			if(chaoxiang==1)
			{
				ss=new Myshot(x+10,y+30,5,chaoxiang);
				alm.add(ss);
			}
			if(chaoxiang==2)
			{
				ss=new Myshot(x-1,y+10,5,chaoxiang);
				alm.add(ss);
			}
			if(chaoxiang==3)
			{
				ss=new Myshot(x+30,y+10,5,chaoxiang);
				alm.add(ss);
			}
			//������Ӻó�ʼ�ӵ�������������ӵ��ƶ��߳�
			Thread t1=new Thread(this.ss);
			t1.start();
		}

		//�ƶ�����
		private void shang() {
			y=y-speed;
		}
		private void xia() {
			y=y+speed;
		}
		private void zuo() {
			x=x-speed;
		}
		private void you() {
			x=x+speed;
		}
	}
	class Animytanks extends Tank implements Runnable
	{
		//������з�̹�˼���
		ArrayList<Animytanks> ats=new ArrayList<Animytanks>();
		//�жϵз�̹���Ƿ���
		boolean isLive=true;
		
		Mypanel m1=null;
		Myshot as=null;
		ArrayList<Myshot>als=new ArrayList<Myshot>();
		public Animytanks(int x, int y, int z, int s) {
			super(x, y, z, s);
		}
		//���ʻ���Mpanel�ĵз�̹�˼��ϵķ���
		public void setanimy(ArrayList<Animytanks> a)
		{
			this.ats=a;
		}
		//�жϵз�̹���Ƿ��غϣ��Ӵ����ķ���falseû�Ӵ���TRUE �Ӵ�
		public boolean istouched()
		{
			boolean b=false;
			Animytanks et;
			//������ǰ���з�̹������
			if(chaoxiang==0)
			{
				
				for(int i=0;i<ats.size();i++)
				{
					et=ats.get(i);
					//�Ǳ��з�̹��
					if(this!=et)
					{
						if(et.chaoxiang==0||et.chaoxiang==1)
						{
							//���������ϽǺ����Ͻ��ڲ�������������
							if(this.x>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
							{
								return true;
							}
							if(this.x+20>et.x&&this.x+20<et.x+20&&this.y>et.y&&this.y<et.y+30)
							{
								return true;
							}
						}
						if(et.chaoxiang==2||et.chaoxiang==3)
						{
							if(this.x>et.x&&this.x<et.x+30&&this.y>et.y&&this.y<et.y+20)
							{
								return true;
							}
							if(this.x+20>et.x&&this.x+20<et.x+30&&this.y>et.y&&this.y<et.y+20)
							{
								return true;
							}
						}
					}
					
				}
			}
			if(chaoxiang==1)
			{
				for(int i=0;i<ats.size();i++)
				{
					et=ats.get(i);
					//�Ǳ��з�̹��
					if(this!=et)
					{
						//���½Ǻ����½��Ƿ�������̹����
						if(et.chaoxiang==0||et.chaoxiang==1)
						{
							if(this.x>et.x&&this.x<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
							{
								return true;
							}
							if(this.x+20>et.x&&this.x+20<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
							{
								return true;
							}
						}
						if(et.chaoxiang==2||et.chaoxiang==3)
						{
							if(this.x>et.x&&this.x<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20)
							{
								return true;
							}
							if(this.x+20>et.x&&this.x+20<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20)
							{
								return true;
							}
						}
					}
				}
			}
			if(chaoxiang==2)
			{
				for(int i=0;i<ats.size();i++)
				{
					et=ats.get(i);
					//�Ǳ��з�̹��
					if(this!=et)
					{
						//���½Ǻ����½��Ƿ�������̹����
						if(et.chaoxiang==0||et.chaoxiang==1)
						{
							if(this.x>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
							{
								return true;
							}
							if(this.x>et.x&&this.x<et.x+20&&this.y+20>et.y&&this.y+20<et.y+30)
							{
								return true;
							}
						}
						if(et.chaoxiang==2||et.chaoxiang==3)
						{
							if(this.x>et.x&&this.x<et.x+30&&this.y>et.y&&this.y<et.y+20)
							{
								return true;
							}
							if(this.x>et.x&&this.x<et.x+30&&this.y+20>et.y&&this.y+20<et.y+20)
							{
								return true;
							}
						}
					}
				}
			}
			if(chaoxiang==3)
			{
				for(int i=0;i<ats.size();i++)
				{
					et=ats.get(i);
					//�Ǳ��з�̹��
					if(this!=et)
					{
						//���½Ǻ����½��Ƿ�������̹����
						if(et.chaoxiang==0||et.chaoxiang==1)
						{
							if(this.x+30>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
							{
								return true;
							}
							if(this.x+30>et.x&&this.x+30<et.x+20&&this.y+20>et.y&&this.y+20<et.y+30)
							{
								return true;
							}
						}
						if(et.chaoxiang==2||et.chaoxiang==3)
						{
							if(this.x+30>et.x&&this.x+30<et.x+30&&this.y>et.y&&this.y<et.y+20)
							{
								return true;
							}
							if(this.x+30>et.x&&this.x+30<et.x+30&&this.y+20>et.y&&this.y+20<et.y+20)
							{
								return true;
							}
						}
					}
				}
			}
			return b;
		}
		
		//����з�̹�˵��ƶ�����
		private void shang() {
			y=y-speed;
		}
		private void xia() {
			y=y+speed;
		}
		private void zuo() {
			x=x-speed;
		}
		private void you() {
			x=x+speed;
		}
		//�з�̹�˵�����ƶ��߳�
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			while(true)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				if(this.getChaoxiang()==0)
				{
//					this.shang();
					//������õз�̹���ߵ�Զһ��
					for(int i=0;i<6;i++)
					{
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						if(y>0&&!istouched())
						{
							this.shang();
						}
					}
				}
				if(this.getChaoxiang()==1)
				{
					for(int i=0;i<6;i++)
					{
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						if(y<300&&!istouched())
						{
							this.xia();
						}
					}
				}
				if(this.getChaoxiang()==2)
				{
					for(int i=0;i<6;i++)
					{
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						if(x>0&&!istouched())
						{
							this.zuo();
						}
					}
				}
				if(this.getChaoxiang()==3)
				{
//					this.you();
					for(int i=0;i<6;i++)
					{
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						if(x<400&&!istouched())
						{
							this.you();
						}
					}
				} 
				this.setChaoxiang((int)(Math.random()*4));
//				if(this.isLive==false)
//				{
//					break;
//				}
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		if(e.getActionCommand().equals("start"))
		{
			//����һֱ ����ʼ���ֲ����Ŀ���
			mp=new Mypanel();
			Thread tmp=new Thread(mp);
			tmp.start();
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
		}
		
	}
}
