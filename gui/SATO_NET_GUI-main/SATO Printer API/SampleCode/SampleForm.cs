using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using SATOPrinterAPI;

namespace SampleCode
{
    public partial class SampleForm : Form
    {
        Printer SATOPrinter = null;
        Driver SATODriver = null;
        bool Minimized = false;
        public SampleForm(string action)
        {
            InitializeComponent();
            SATOPrinter = new Printer();
            SATODriver = new Driver();
            cbInterfaces.SelectedIndex = 0;

            if (action.ToLower() == "minimized")
                Minimized = true;
        }

        private void SampleForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            SATOPrinter.Disconnect();
            chkSocketServer.Checked = false;
        }

        private void SampleForm_Load(object sender, EventArgs e)
        {
            this.Text = "SATO Printer API (version: " + Application.ProductVersion + ")";
            lblDriverVersion.Text = "";
        }

        private void SampleForm_Resize(object sender, EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {

            }
        }

        private void SampleForm_Shown(object sender, EventArgs e)
        {
            if (Minimized)
                WindowState = FormWindowState.Minimized;
        }

        private void notifyIcon_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            Show();
            this.WindowState = FormWindowState.Normal;
            notifyIcon.Visible = false;
        }

        private void btnClearSend_Click(object sender, EventArgs e)
        {
            txtSend.Text = "";
            rtxtRecv.Text = "";
            txtTotalRecvBytes.Text = "0";
            txtSend.Focus();
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtSend.Text)) return;

            MouseWait = true;
            try
            {
                byte[] cmddata = Utils.StringToByteArray(ControlCharReplace(txtSend.Text));
                if (cbInterfaces.SelectedIndex == 4)
                {
                    if (cbDriver.SelectedValue != null)
                    {
                        SATODriver.SendRawData(cbDriver.SelectedValue.ToString(), cmddata);
                    }
                    else
                    {
                        MessageBox.Show("Please select SATO printer driver from drop down list");
                    }
                }
                else
                {
                    SetInterface();
                    SATOPrinter.Send(cmddata);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            MouseWait = false;
        }

        private void btnLoadFile_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog1 = new OpenFileDialog();

            openFileDialog1.RestoreDirectory = false;
            openFileDialog1.Filter = "Command file (*.txt,*.prn)|*.txt;*.prn|All files (*.*)|*.*";
            openFileDialog1.FilterIndex = 0;

            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                try
                {
                    if (System.IO.File.Exists(openFileDialog1.FileName))
                    {
                        byte[] cmddata = System.IO.File.ReadAllBytes(openFileDialog1.FileName);
                        MouseWait = true;
                        try
                        {
                            txtSend.Text = ControlCharConvert(Utils.ByteArrayToString(cmddata));
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show(ex.Message);
                        }
                        MouseWait = false;
                    }
                    else
                    {
                        MessageBox.Show("Error: file not found.");
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error: " + ex.Message);
                }
            }
        }

        private void btnQuery_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(txtSend.Text)) return;

            MouseWait = true;
            try
            {
                rtxtRecv.Text = "";
                txtTotalRecvBytes.Text = "0";
                SetInterface();
                byte[] cmddata = Utils.StringToByteArray(ControlCharReplace(txtSend.Text));
                byte[] receiveData = SATOPrinter.Query(cmddata);
                
                if (receiveData != null)
                {
                    AppendRecvText(receiveData, true);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            MouseWait = false;
        }

        private void btnExport_Click(object sender, EventArgs e)
        {
            string contents = ControlCharReplace(rtxtRecv.Text);
            if (!string.IsNullOrEmpty(contents))
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.RestoreDirectory = false;
                saveFileDialog.Title = "Browse Files";
                saveFileDialog.Filter = "Command file (*.prn)|*.prn|All files (*.*)|*.*";
                saveFileDialog.CheckFileExists = false;
                saveFileDialog.CheckPathExists = true;
                saveFileDialog.FilterIndex = 0;

                if (saveFileDialog.ShowDialog() == DialogResult.OK)
                {
                    System.IO.File.WriteAllText(saveFileDialog.FileName, contents.Substring(0, contents.Length -2));
                }
            }
            else
            {
                MessageBox.Show(this, "No data to be saved");
            }
        }

        private void cbInterfaces_SelectedIndexChanged(object sender, EventArgs e)
        {
            btnQuery.Enabled = true;
            groupBox2.Enabled = true;
            switch (cbInterfaces.SelectedIndex)
            {
                case 0:
                    panelSocket.Show();
                    panelUSB.Hide();
                    pnlDriver.Hide();
                    chkPermanent.Enabled = true;
                    chkPermanent.Checked = false;
                    txtTimeout.Enabled = true;
                    break;
                case 1:
                    RefreshCOM_USBList();
                    panelUSB.Show();
                    panelSocket.Hide();
                    pnlDriver.Hide();
                    chkPermanent.Enabled = true;
                    chkPermanent.Checked = false;
                    txtTimeout.Enabled = true;
                    break;
                case 2:
                    RefreshCOM_USBList();
                    panelUSB.Show();
                    panelSocket.Hide();
                    pnlDriver.Hide();
                    chkPermanent.Enabled = true;
                    chkPermanent.Checked = false;
                    txtTimeout.Enabled = true;
                    break;
                case 3:
                    RefreshCOM_USBList();
                    panelUSB.Show();
                    panelSocket.Hide();
                    pnlDriver.Hide();
                    chkPermanent.Enabled = true;
                    chkPermanent.Checked = false;
                    txtTimeout.Enabled = true;
                    break;
                case 4:
                    panelUSB.Hide();
                    panelSocket.Hide();
                    pnlDriver.Show();
                    chkPermanent.Enabled = false;
                    chkPermanent.Checked = false;
                    btnQuery.Enabled = false;
                    groupBox2.Enabled = false;
                    txtTimeout.Enabled = false;
                    break;
            }

            if (chkPermanent.Checked)
            {
                SATOPrinter.Disconnect();
            }
        }

        private void cbTCPIP_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (cbTCPIP.SelectedValue != null)
            {
                txtIP.Text = cbTCPIP.SelectedValue.ToString();
            }
        }

        private void cbDriver_SelectedIndexChanged(object sender, EventArgs e)
        {
            MouseWait = true;
            lblDriverVersion.Text = SATODriver.GetVersion(cbDriver.Text);
            MouseWait = false;
        }
        
        private void btn_TCPIPRefresh_Click(object sender, EventArgs e)
        {
            btn_TCPIPRefresh.Enabled = false;
            RefreshTCPIPList();
            btn_TCPIPRefresh.Enabled = true;
        }

        private void btnRefresh_Click(object sender, EventArgs e)
        {
            btnRefresh.Enabled = false;
            RefreshCOM_USBList();
            btnRefresh.Enabled = true;
        }

        private void btnRefDriver_Click(object sender, EventArgs e)
        {
            btnRefDriver.Enabled = false;
            Refresh_DriverList();
            btnRefDriver.Enabled = true;
        }

        private void chkPermanent_CheckedChanged(object sender, EventArgs e)
        {
            if (chkPermanent.Checked)
            {
                Cursor = Cursors.WaitCursor;
                try
                {
                    SATOPrinter.PermanentConnect = true;
                    SATOPrinter.ByteAvailable += AsynDataIn;
                    SetInterface();
                    SATOPrinter.Connect();
                    btnQuery.Enabled = false;
                    txtTimeout.Enabled = false;
                    cbInterfaces.Enabled = false;
                    panelUSB.Enabled = false;
                    panelSocket.Enabled = false;
                }
                catch(Exception ex)
                {
                    MessageBox.Show(ex.Message);
                    chkPermanent.Checked = false;
                }
                finally
                {
                    Cursor = Cursors.Default;
                }
            }
            else
            {
                try
                {
                    SATOPrinter.ByteAvailable -= AsynDataIn;
                    SATOPrinter.PermanentConnect = false;
                    SATOPrinter.Disconnect();
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
                btnQuery.Enabled = true;
                cbInterfaces.Enabled = true;
                panelUSB.Enabled = true;
                panelSocket.Enabled = true;
                txtTimeout.Enabled = true;
            }
        }

        SocketServer SATOServer = null;
        private void chkSocketServer_CheckedChanged(object sender, EventArgs e)
        {
            try
            {
                if (chkSocketServer.Checked)
                {
                    if(SATOServer != null)
                        SATOServer.Stop();

                    SATOServer = new SocketServer();
                    // implement SSL to websocket (wss) required to supply a certificate. browser default block the untrust certificate, you need to import the certificate to "Trusted Root Certification Authorities" in order to work.
                    //SATOServer.Certificate = new System.Security.Cryptography.X509Certificates.X509Certificate2(string.Format("{0}localhost_cert.pfx", AppDomain.CurrentDomain.BaseDirectory), "0310");
                    SATOServer.Start();
                }
                else
                {
                    if (SATOServer != null)
                    {
                        SATOServer.Stop();
                        SATOServer = null;
                    }
                }
            }
            catch
            {
                MessageBox.Show(this, "Socket server failed to start, please ensure the port number not being used");
                chkSocketServer.Checked = false;
            }
        }

        private void txtSend_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter && chkAutoSend.Checked)
            {
                btnSend.PerformClick();
                System.Threading.Thread.Sleep(500);
                btnClear.PerformClick();
            }
        }

        private void AsynDataIn(object sender, Printer.ByteAvailableEventArgs e)
        {
            AppendRecvText(e.Data, false);
        }

        private bool MouseWait
        {
            set
            {
                if (value)
                    Cursor = Cursors.WaitCursor;
                else
                    Cursor = Cursors.Default; 
            }
        }

        private void RefreshCOM_USBList()
        {
            cbUSBPorts.DataSource = null;
            cbUSBPorts.Items.Clear();
            Dictionary<string, string> cbObj = new Dictionary<string, string>();
            switch (cbInterfaces.SelectedIndex)
            {
                case 1:
                    foreach (string comport in SATOPrinter.GetCOMList())
                    {
                        cbObj.Add(comport, comport);
                    }
                    break;
                case 2:
                    foreach (string lptport in SATOPrinter.GetLPTList())
                    {
                        cbObj.Add(lptport, lptport);
                    }
                    break;
                case 3:
                    
                    foreach (Printer.USBInfo usbPorts in SATOPrinter.GetUSBList())
                    {
                        cbObj.Add(usbPorts.PortID, usbPorts.Name);
                    }
                    break;
            }

            if (cbObj.Count > 0)
            {
                cbUSBPorts.DataSource = new BindingSource(cbObj, null);
                cbUSBPorts.DisplayMember = "Value";
                cbUSBPorts.ValueMember = "Key";
                cbUSBPorts.SelectedIndex = 0;
            }
        }

        private void RefreshTCPIPList()
        {
            cbTCPIP.DisplayMember = "Name";
            cbTCPIP.ValueMember = "IPAddress";
            cbTCPIP.DataSource = new BindingSource(SATOPrinter.GetTCPIPList(), null);
        }

        private void Refresh_DriverList()
        {
            cbDriver.DisplayMember = "DriverName";
            cbDriver.ValueMember = "DriverName";
            cbDriver.DataSource = new BindingSource(SATODriver.GetDriverList(), null);
        }

        private void SetInterface()
        {
            try
            {
                int timeOut = int.Parse(txtTimeout.Text);
                if (timeOut <= 1000)
                    timeOut = 1000;
                SATOPrinter.Timeout = timeOut;

                switch (cbInterfaces.SelectedIndex)
                {
                    case 0: //Socket
                        SATOPrinter.Interface = Printer.InterfaceType.TCPIP;
                        SATOPrinter.TCPIPAddress = txtIP.Text;
                        SATOPrinter.TCPIPPort = txtPort.Text;
                        break;
                    case 1: //COM
                        SATOPrinter.Interface = Printer.InterfaceType.COM;;
                        if (cbUSBPorts.SelectedItem != null)
                            SATOPrinter.COMPort = ((KeyValuePair<string, string>)cbUSBPorts.SelectedItem).Key;
                        break;
                    case 2: //LPT
                        SATOPrinter.Interface = Printer.InterfaceType.LPT;
                        if (cbUSBPorts.SelectedItem != null)
                            SATOPrinter.LPTPort = ((KeyValuePair<string, string>)cbUSBPorts.SelectedItem).Key;
                        break;
                    case 3: //USB
                        SATOPrinter.Interface = Printer.InterfaceType.USB;
                        if (cbUSBPorts.SelectedItem != null)
                            SATOPrinter.USBPortID = ((KeyValuePair<string, string>)cbUSBPorts.SelectedItem).Key;
                        break;
                    default:
                        MessageBox.Show("Error : Invalid Interface Selection!");
                        break;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void AppendRecvText(byte[] data, bool empty)
        {
            if (InvokeRequired)
            {
                this.Invoke(new Action<byte[],bool>(AppendRecvText), new object[] { data,empty });
                return;
            }
            if (empty)
                rtxtRecv.Text = "";

            rtxtRecv.WordWrap = true;

            string smsg = ControlCharConvert(Utils.ByteArrayToString(data));
            if(chkPermanent.Checked)
                rtxtRecv.AppendText(DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss:fff") + Environment.NewLine);
            
            rtxtRecv.AppendText(smsg + Environment.NewLine);
            txtTotalRecvBytes.Text = data.Length.ToString();
        }

        private string ControlCharConvert(string data)
        {
            Dictionary<char, string> chrList = ControlCharList().ToDictionary(x => x.Value, x => x.Key);
            foreach (char key in chrList.Keys)
            {
                data = data.Replace(key.ToString(), chrList[key]);
            }
            return data;
        }

        private string ControlCharReplace(string data)
        {
            Dictionary<string, char> chrList = ControlCharList();
            foreach (string key in chrList.Keys)
            {
                data = data.Replace(key, chrList[key].ToString());
            }
            return data;
        }

        private Dictionary<string, char> ControlCharList()
        {
            Dictionary<string, char> ctr = new Dictionary<string, char>();
            ctr.Add("[NUL]", '\u0000');
            ctr.Add("[SOH]", '\u0001');
            ctr.Add("[STX]", '\u0002');
            ctr.Add("[ETX]", '\u0003');
            ctr.Add("[EOT]", '\u0004');
            ctr.Add("[ENQ]", '\u0005');
            ctr.Add("[ACK]", '\u0006');
            ctr.Add("[BEL]", '\u0007');
            ctr.Add("[BS]", '\u0008');
            ctr.Add("[HT]", '\u0009');
            ctr.Add("[LF]", '\u000A');
            ctr.Add("[VT]", '\u000B');
            ctr.Add("[FF]", '\u000C');
            ctr.Add("[CR]", '\u000D');
            ctr.Add("[SO]", '\u000E');
            ctr.Add("[SI]", '\u000F');
            ctr.Add("[DLE]", '\u0010');
            ctr.Add("[DC1]", '\u0011');
            ctr.Add("[DC2]", '\u0012');
            ctr.Add("[DC3]", '\u0013');
            ctr.Add("[DC4]", '\u0014');
            ctr.Add("[NAK]", '\u0015');
            ctr.Add("[SYN]", '\u0016');
            ctr.Add("[ETB]", '\u0017');
            ctr.Add("[CAN]", '\u0018');
            ctr.Add("[EM]", '\u0019');
            ctr.Add("[SUB]", '\u001A');
            ctr.Add("[ESC]", '\u001B');
            ctr.Add("[FS]", '\u001C');
            ctr.Add("[GS]", '\u001D');
            ctr.Add("[RS]", '\u001E');
            ctr.Add("[US]", '\u001F');
            ctr.Add("[DEL]", '\u007F');
            return ctr;
        }

        private void txtTimeout_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar))
            {
                e.Handled = true;
            }
        }
    }
}
