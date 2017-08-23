//自己的坦克可控制速度移动
//多少台敌方坦克出现
//己方坦克能发子弹、子弹数量
//己方击中敌方会消灭
//敌方能随机移动，发射子弹
//敌方、己方坦克活动范围限制  
//敌方被打中要爆炸
//我方被打中要爆炸
//
//al:敌方坦克集           als：敌方子弹集         alm：我方子弹
package com.tanks;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.*;
import java.math.*;
public class Demo1 extends JFrame implements ActionListener{
	Mypanel mp=null;
	Mystartpanel msp=null;
	//控制闪烁效果的参数
	int time=0;
	JMenuBar jmb=null;
	JMenu jm=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
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
		
		jm=new JMenu("游戏菜单");
		jm.setMnemonic('G');
		jmb.add(jm);
		
		jmi1=new JMenuItem("开始游戏");
		jm.add(jmi1);
		jmi1.addActionListener(this);
		jmi1.setActionCommand("start");
		
		jmi2=new JMenuItem("保存游戏");
		jm.add(jmi2);
		
		this.add(msp);
		//设置窗口属性
		this.setTitle("坦克大战之");
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
				Font myfont=new Font("微软雅黑",0, 30);
				g.setFont(myfont);
				g.drawString("第一关", 150, 150);
			}			
		}

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(true)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				time++;
				this.repaint();
			}
		}
	}
	class Mypanel extends JPanel implements KeyListener,Runnable
	{
		//声明
//		Image image=null;
		Mytank t=null;
		Animytanks et;
		//声明敌方坦克集合
		ArrayList<Animytanks> al;
		ArrayList<Boom> aboom=new ArrayList<Boom>();
		Record record=new Record();
		//构造函数
		public Mypanel()
		{
			//初始化我方坦克位置：x、y、朝向、速度
			t=new Mytank(180, 270,0,5);
			//初始化敌方坦克数量以及位置{坦克集合}
			al=new ArrayList<Animytanks>();
			int e=3;
			for(int i=0;i<e;i++)
			{
				Animytanks et=null;
				et=new Animytanks((i+1)*90,0,1,5);
				//把敌方坦克集合添加到  al中在Animytanks类里istouched（）方法里用到
				et.setanimy(al);
				//启动敌方坦克移动的线程（值得注意的是：每一辆敌方坦克都要启动一个线程）
				Thread t4=new Thread(et);
				t4.start();
				//添加到敌方坦克到集合
				al.add(et);
			}
		}
		//敌我信息函数
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
			//画出活动范围
			g.fillRect(0, 0, 420, 320);
			
			//击杀情况
			Font myfont=new Font("微软雅黑",0, 30);
			g.drawString("我的总成绩", 450, 50);
			g.setColor(Color.red);
			this.drawmy(450, 75, g, 0, 0);
			g.drawString(record.getHtnum()+"", 480, 95);
			
			//敌我信息
			this.p(g);
			
			//画我方坦克
			if(t.isLive==true)
			{
				g.setColor(Color.cyan);
				this.drawmy(t.getX(),t.getY(), g, t.getChaoxiang(),t.getSpeed());
			}
			this.repaint();
			//画己方子弹
			for(int j=0;j<t.alm.size();j++)
			{
				t.ss=(Myshot)t.alm.get(j);
				if(t.ss!=null&&t.ss.isLive==true)
				{
					g.fill3DRect(t.ss.x, t.ss.y, 2, 2, false);
				}
				//删除出界子弹
				if(t.ss!=null&&t.ss.isLive==false)
				{
					t.alm.remove(t.ss);
				}
			}	
			//画敌方坦克
			for(int i=0;i<al.size();i++)
			{
				Myshot enemyshot=null;
				g.setColor(Color.red);
				et=(Animytanks)al.get(i);
				//只有活着才画出坦克
				if(et.isLive==true)
				{
					this.drawmy(et.getX(), et.getY(), g, et.getChaoxiang(), et.getSpeed());
					//画出敌方子弹
					for(int j=0;j<et.als.size();j++)
					{
						//取出子弹
						enemyshot=et.als.get(j);
						//子弹存活
						if(enemyshot.isLive==true)
						{
							if(enemyshot.isLive)
							{
								g.draw3DRect(enemyshot.x, enemyshot.y, 2, 2, false);
							}
						}
						//子弹死亡
						else
						{
							et.als.remove(enemyshot);
						}
					}
				}
				//死亡坦克删除
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
			//朝向上、下 、左、右是坦克形态
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
			//上下朝向
			if(t.chaoxiang==0||t.chaoxiang==1)
			{
				//击中
				if(ms.x>t.x&&ms.x<t.x+20&&ms.y>t.y&&ms.y<t.y+30)
				{
					//子弹死亡
					ms.isLive=false;
					//敌方死亡
					t.isLive=false;
					record.hmt();
					Boom boom=new Boom(t.x,t.y);
					aboom.add(boom);
				}
			}
			//左右朝向
			if(t.chaoxiang==2||t.chaoxiang==3)
			{
				if(ms.x>t.x&&ms.x<t.x+30&&ms.y>t.y&&ms.y<t.y+20)
				{
					//子弹死亡
					ms.isLive=false;
					//敌方死亡
					t.isLive=false;
					record.hmt();
					Boom boom=new Boom(t.x,t.y);
					aboom.add(boom);
				}
			}
		}
		//我方子弹是否击中敌方坦克
		public void hitanimy(Myshot ss,Animytanks et)
		{
			//敌方上下朝向
			if(et.chaoxiang==0||et.chaoxiang==1)
			{
				//击中
				if(ss.x>et.x&&ss.x<et.x+20&&ss.y>et.y&&ss.y<et.y+30)
				{
					//子弹死亡
					ss.isLive=false;
					//敌方死亡
					et.isLive=false;
					record.het();
					record.heihei();
					Boom boom=new Boom(et.x,et.y);
					aboom.add(boom);
				}
			}
			//敌方左右朝向
			if(et.chaoxiang==2||et.chaoxiang==3)
			{
				if(ss.x>et.x&&ss.x<et.x+30&&ss.y>et.y&&ss.y<et.y+20)
				{
					//子弹死亡
					ss.isLive=false;
					//敌方死亡
					et.isLive=false;
					record.het();
					record.heihei();
					Boom boom=new Boom(et.x,et.y);
					aboom.add(boom);
				}
			}
		}

		//按键监听接口实现
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO 自动生成的方法存根
			if(arg0.getKeyCode()==38)
			{
				//移动
				if(t.y>0)
				{
					t.shang();
					//坦克朝向
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
			//j键按下时执行shotanimy（）方法和子弹移动线程
			if(arg0.getKeyCode()==74)
			{
				//先shotanimy构建初始子弹      后启动子弹移动线程
				if(t.alm.size()<=5)
				{
					t.shotanimy();
				}
			}
			
		}
		

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO 自动生成的方法存根
			
		}



		@Override
		//建立repaint线程            会在Mypanel的构造函数中启动该线程
		public void run() {
			// TODO 自动生成的方法存根
			while(true)
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				//我方子弹击中敌方后的方法
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
				//控制敌方是否加入新子弹
				for(int i=0;i<al.size();i++)
				{
					Animytanks et=al.get(i);
					if(et.isLive)
					{
						//规定每个坦克子弹不小于3
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
							//子弹移动线程
							Thread t7=new Thread(s);
							t7.start();
						}
					}
				}
				
				//敌方子弹是否击中我方
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
			// TODO 自动生成的构造函数存根
		}
		public void run()
		{
			while(true)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
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
				//判断子弹是否活着
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
		//我方坦克的子弹方法     在我方坦克的坐标基础上（x、y） 构建初始的子弹    此方法会在j键监听中用到
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
			//上面添加好初始子弹后，启动下面的子弹移动线程
			Thread t1=new Thread(this.ss);
			t1.start();
		}

		//移动方法
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
		//定义出敌方坦克集合
		ArrayList<Animytanks> ats=new ArrayList<Animytanks>();
		//判断敌方坦克是否存活
		boolean isLive=true;
		
		Mypanel m1=null;
		Myshot as=null;
		ArrayList<Myshot>als=new ArrayList<Myshot>();
		public Animytanks(int x, int y, int z, int s) {
			super(x, y, z, s);
		}
		//访问画板Mpanel的敌方坦克集合的方法
		public void setanimy(ArrayList<Animytanks> a)
		{
			this.ats=a;
		}
		//判断敌方坦克是否重合（接触）的方法false没接触，TRUE 接触
		public boolean istouched()
		{
			boolean b=false;
			Animytanks et;
			//本（当前）敌方坦克向上
			if(chaoxiang==0)
			{
				
				for(int i=0;i<ats.size();i++)
				{
					et=ats.get(i);
					//非本敌方坦克
					if(this!=et)
					{
						if(et.chaoxiang==0||et.chaoxiang==1)
						{
							//本机的左上角和右上角在不在其他机里面
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
					//非本敌方坦克
					if(this!=et)
					{
						//左下角和右下角是否在其他坦克里
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
					//非本敌方坦克
					if(this!=et)
					{
						//左下角和右下角是否在其他坦克里
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
					//非本敌方坦克
					if(this!=et)
					{
						//左下角和右下角是否在其他坦克里
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
		
		//定义敌方坦克的移动方法
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
		//敌方坦克的随机移动线程
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(true)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if(this.getChaoxiang()==0)
				{
//					this.shang();
					//如果想让敌方坦克走的远一点
					for(int i=0;i<6;i++)
					{
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
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
							// TODO 自动生成的 catch 块
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
							// TODO 自动生成的 catch 块
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
							// TODO 自动生成的 catch 块
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
		// TODO 自动生成的方法存根
		if(e.getActionCommand().equals("start"))
		{
			//考虑一直 按开始这种操作的可能
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
