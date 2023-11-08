﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using System.Windows.Forms;
using SATOPrinterAPI;

namespace SATO_NET_GUI
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            

           
            


            //SATOPrinter.TestPrint();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            string inputText = textBox1.Text;
            if (inputText.Length == 0)
            {
                MessageBox.Show("Work ID cannot be empty! Please try again.");
            }
            else
            {
                MessageBox.Show("You Entered: " + inputText);
                Printer SATOPrinter = new Printer();
                List<Printer.USBInfo> USBPorts = SATOPrinter.GetUSBList();
                SATOPrinter.Interface = Printer.InterfaceType.USB;
                String foundPortID="";
                foreach (Printer.USBInfo item in USBPorts)
                {
                    Console.WriteLine(item.Name);
                    Console.WriteLine(item.PortID.ToString());
                    foundPortID = item.PortID.ToString(); 
                }
                //"\\\\?\\usb#vid_0828&pid_0084#5&6f2a2c3&0&1#{a5dcbf10-6530-11d2-901f-00c04fb951ed}"
                
                if (foundPortID.Length == 0)
                {
                    MessageBox.Show("Printer not connected!");
                }
                else
                {
                    SATOPrinter.USBPortID = foundPortID;
                    SATOPrinter.PermanentConnect = true;
                    SATOPrinter.Connect();
                    string chr27 = ((char)27).ToString();
                    string hashedString = ReplaceLast8CharsWithOriginal(inputText);
                    Console.WriteLine(hashedString);
                    string fileName = "test.txt";
                    if (File.Exists(fileName))
                    {
                        File.Delete(fileName);
                    }
                    using (StreamWriter sw = new StreamWriter(File.Open(fileName, FileMode.Create), Encoding.UTF8))
                    {
                        sw.WriteLine(chr27 + "A");
                        sw.WriteLine(chr27 + "V100" + chr27 + "H50" + chr27 + "XM" + "Work ID:" + inputText);
                        sw.WriteLine(chr27 + "IP0e:h,epc:" + hashedString + ";");
                        sw.WriteLine(chr27 + "Q1");
                        sw.WriteLine(chr27 + "Z");
                    }

                    byte[] cmdData = File.ReadAllBytes(fileName);
                    SATOPrinter.Send(cmdData);
                }
            }
        }
        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }
        public static string ReplaceLast8CharsWithOriginal(string input)
        {
            using (MD5 md5 = MD5.Create())
            {
                byte[] inputBytes = Encoding.UTF8.GetBytes(input);
                byte[] hashBytes = md5.ComputeHash(inputBytes);

                // Convert the hash bytes to a hexadecimal string.
                StringBuilder hashString = new StringBuilder();
                for (int i = 0; i < hashBytes.Length; i++)
                {
                    hashString.Append(hashBytes[i].ToString("x2"));
                }

                // Replace the last 8 characters of the hash with the original input string.
                if (hashString.Length >= 8)
                {
                    int startIndex = hashString.Length - 8;
                    hashString.Remove(startIndex, 8);
                    hashString.Insert(startIndex, input);
                }

                return hashString.ToString();
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            textBox1.Text = "";
        }
    }
}
