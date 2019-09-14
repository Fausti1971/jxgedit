package device;

import adress.InvalidXGAdressException;
import adress.XGAdress;
import msg.XGMessageDumpRequest;
import msg.XGMessageParameterChange;
import value.WrongXGValueTypeException;
import value.XGIntegerValue;
import value.XGValue;

public class XGTextReader extends Thread
{
	private static final XGAdress MSB = new XGAdress(8,0,1), LSB = new XGAdress(8,0,2), PRG = new XGAdress(8,0,3), TEXT = new XGAdress(6,0,0);
	private static int DELAY = 100;
	
	public void run()
	{	for(int msb = 0; msb <= 127; msb++)
		{	XGValue v;
			try
			{	v = new XGIntegerValue(MSB);
				v.setContent(msb);
				new XGMessageParameterChange(v).transmit();
				Thread.sleep(DELAY);
			}
			catch(InvalidXGAdressException | InterruptedException | WrongXGValueTypeException e)
			{	e.printStackTrace();
			}
			for(int lsb = 0; lsb <= 127; lsb++)
			{	try
				{	v = new XGIntegerValue(LSB);
					v.setContent(lsb);
					new XGMessageParameterChange(v).transmit();
					Thread.sleep(DELAY);
				}
				catch(InvalidXGAdressException | InterruptedException | WrongXGValueTypeException e)
				{	e.printStackTrace();
				}
				for(int prg = 0; prg <= 127; prg++)
				{	try
					{	v = new XGIntegerValue(PRG);
						v.setContent(prg);
						new XGMessageParameterChange(v).transmit();
						Thread.sleep(DELAY);
						new XGMessageDumpRequest(TEXT).transmit();
						Thread.sleep(300);
						System.out.println("MSB: " + msb + ", LSB: " + lsb + ", PRG: " + prg + ", Text: " + XGValue.getValue(TEXT));
					}
					catch(InvalidXGAdressException | InterruptedException | WrongXGValueTypeException e)
					{	e.printStackTrace();
					}
				}
			}
		}
	}
	
}
