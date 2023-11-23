namespace SampleCode
{
    partial class SampleForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SampleForm));
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.btnExport = new System.Windows.Forms.Button();
            this.rtxtRecv = new System.Windows.Forms.TextBox();
            this.groupBox6 = new System.Windows.Forms.GroupBox();
            this.label7 = new System.Windows.Forms.Label();
            this.txtTotalRecvBytes = new System.Windows.Forms.TextBox();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.txtTimeout = new System.Windows.Forms.TextBox();
            this.cbInterfaces = new System.Windows.Forms.ComboBox();
            this.panelUSB = new System.Windows.Forms.Panel();
            this.btnRefresh = new System.Windows.Forms.Button();
            this.cbUSBPorts = new System.Windows.Forms.ComboBox();
            this.chkSocketServer = new System.Windows.Forms.CheckBox();
            this.pnlDriver = new System.Windows.Forms.Panel();
            this.lblDriverVersion = new System.Windows.Forms.Label();
            this.btnRefDriver = new System.Windows.Forms.Button();
            this.cbDriver = new System.Windows.Forms.ComboBox();
            this.chkAutoSend = new System.Windows.Forms.CheckBox();
            this.chkPermanent = new System.Windows.Forms.CheckBox();
            this.panelSocket = new System.Windows.Forms.Panel();
            this.btn_TCPIPRefresh = new System.Windows.Forms.Button();
            this.cbTCPIP = new System.Windows.Forms.ComboBox();
            this.txtIP = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.txtPort = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btnLoadFile = new System.Windows.Forms.Button();
            this.txtSend = new System.Windows.Forms.RichTextBox();
            this.btnClear = new System.Windows.Forms.Button();
            this.btnQuery = new System.Windows.Forms.Button();
            this.btnSend = new System.Windows.Forms.Button();
            this.notifyIcon = new System.Windows.Forms.NotifyIcon(this.components);
            this.groupBox2.SuspendLayout();
            this.groupBox6.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.panelUSB.SuspendLayout();
            this.pnlDriver.SuspendLayout();
            this.panelSocket.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox2
            // 
            this.groupBox2.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.groupBox2.Controls.Add(this.btnExport);
            this.groupBox2.Controls.Add(this.rtxtRecv);
            this.groupBox2.Controls.Add(this.groupBox6);
            this.groupBox2.Location = new System.Drawing.Point(-1, 305);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(740, 191);
            this.groupBox2.TabIndex = 5;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Receive Data";
            // 
            // btnExport
            // 
            this.btnExport.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnExport.Location = new System.Drawing.Point(576, 32);
            this.btnExport.Name = "btnExport";
            this.btnExport.Size = new System.Drawing.Size(134, 25);
            this.btnExport.TabIndex = 24;
            this.btnExport.Text = "Save To File";
            this.btnExport.UseVisualStyleBackColor = true;
            this.btnExport.Click += new System.EventHandler(this.btnExport_Click);
            // 
            // rtxtRecv
            // 
            this.rtxtRecv.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.rtxtRecv.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.rtxtRecv.HideSelection = false;
            this.rtxtRecv.Location = new System.Drawing.Point(13, 32);
            this.rtxtRecv.Multiline = true;
            this.rtxtRecv.Name = "rtxtRecv";
            this.rtxtRecv.ReadOnly = true;
            this.rtxtRecv.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.rtxtRecv.Size = new System.Drawing.Size(536, 145);
            this.rtxtRecv.TabIndex = 23;
            // 
            // groupBox6
            // 
            this.groupBox6.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox6.Controls.Add(this.label7);
            this.groupBox6.Controls.Add(this.txtTotalRecvBytes);
            this.groupBox6.Location = new System.Drawing.Point(581, 93);
            this.groupBox6.Name = "groupBox6";
            this.groupBox6.Size = new System.Drawing.Size(129, 61);
            this.groupBox6.TabIndex = 16;
            this.groupBox6.TabStop = false;
            this.groupBox6.Text = "Received Bytes";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(6, 16);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(40, 13);
            this.label7.TabIndex = 16;
            this.label7.Text = "Total : ";
            // 
            // txtTotalRecvBytes
            // 
            this.txtTotalRecvBytes.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.txtTotalRecvBytes.Location = new System.Drawing.Point(16, 30);
            this.txtTotalRecvBytes.Margin = new System.Windows.Forms.Padding(2);
            this.txtTotalRecvBytes.Name = "txtTotalRecvBytes";
            this.txtTotalRecvBytes.ReadOnly = true;
            this.txtTotalRecvBytes.Size = new System.Drawing.Size(93, 20);
            this.txtTotalRecvBytes.TabIndex = 15;
            // 
            // groupBox4
            // 
            this.groupBox4.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.groupBox4.Controls.Add(this.label4);
            this.groupBox4.Controls.Add(this.label1);
            this.groupBox4.Controls.Add(this.txtTimeout);
            this.groupBox4.Controls.Add(this.cbInterfaces);
            this.groupBox4.Controls.Add(this.panelUSB);
            this.groupBox4.Controls.Add(this.chkSocketServer);
            this.groupBox4.Controls.Add(this.pnlDriver);
            this.groupBox4.Controls.Add(this.chkAutoSend);
            this.groupBox4.Controls.Add(this.chkPermanent);
            this.groupBox4.Controls.Add(this.panelSocket);
            this.groupBox4.Location = new System.Drawing.Point(-1, 1);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.Size = new System.Drawing.Size(740, 140);
            this.groupBox4.TabIndex = 4;
            this.groupBox4.TabStop = false;
            this.groupBox4.Text = "Connection Setup";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(365, 116);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(127, 13);
            this.label4.TabIndex = 25;
            this.label4.Text = "Connection Timeout (ms):";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(10, 35);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(76, 13);
            this.label1.TabIndex = 1;
            this.label1.Text = "Interface Type";
            // 
            // txtTimeout
            // 
            this.txtTimeout.Location = new System.Drawing.Point(498, 113);
            this.txtTimeout.Name = "txtTimeout";
            this.txtTimeout.Size = new System.Drawing.Size(41, 20);
            this.txtTimeout.TabIndex = 24;
            this.txtTimeout.Text = "2500";
            this.txtTimeout.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtTimeout_KeyPress);
            // 
            // cbInterfaces
            // 
            this.cbInterfaces.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbInterfaces.FormattingEnabled = true;
            this.cbInterfaces.Items.AddRange(new object[] {
            "TCP/IP Socket",
            "Serial COM",
            "Parallel LPT",
            "USB Port",
            "Driver"});
            this.cbInterfaces.Location = new System.Drawing.Point(92, 31);
            this.cbInterfaces.Name = "cbInterfaces";
            this.cbInterfaces.Size = new System.Drawing.Size(121, 21);
            this.cbInterfaces.TabIndex = 0;
            this.cbInterfaces.SelectedIndexChanged += new System.EventHandler(this.cbInterfaces_SelectedIndexChanged);
            // 
            // panelUSB
            // 
            this.panelUSB.Controls.Add(this.btnRefresh);
            this.panelUSB.Controls.Add(this.cbUSBPorts);
            this.panelUSB.Location = new System.Drawing.Point(220, 20);
            this.panelUSB.Name = "panelUSB";
            this.panelUSB.Size = new System.Drawing.Size(421, 35);
            this.panelUSB.TabIndex = 11;
            // 
            // btnRefresh
            // 
            this.btnRefresh.Image = ((System.Drawing.Image)(resources.GetObject("btnRefresh.Image")));
            this.btnRefresh.Location = new System.Drawing.Point(217, 12);
            this.btnRefresh.Name = "btnRefresh";
            this.btnRefresh.Size = new System.Drawing.Size(23, 23);
            this.btnRefresh.TabIndex = 3;
            this.btnRefresh.UseVisualStyleBackColor = true;
            this.btnRefresh.Click += new System.EventHandler(this.btnRefresh_Click);
            // 
            // cbUSBPorts
            // 
            this.cbUSBPorts.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbUSBPorts.FormattingEnabled = true;
            this.cbUSBPorts.Location = new System.Drawing.Point(25, 12);
            this.cbUSBPorts.Name = "cbUSBPorts";
            this.cbUSBPorts.Size = new System.Drawing.Size(182, 21);
            this.cbUSBPorts.TabIndex = 1;
            // 
            // chkSocketServer
            // 
            this.chkSocketServer.AutoSize = true;
            this.chkSocketServer.Location = new System.Drawing.Point(13, 115);
            this.chkSocketServer.Name = "chkSocketServer";
            this.chkSocketServer.Size = new System.Drawing.Size(282, 17);
            this.chkSocketServer.TabIndex = 13;
            this.chkSocketServer.Text = "Socket Server (ws://localhost:8055/SATOPrinterAPI))";
            this.chkSocketServer.UseVisualStyleBackColor = true;
            this.chkSocketServer.CheckedChanged += new System.EventHandler(this.chkSocketServer_CheckedChanged);
            // 
            // pnlDriver
            // 
            this.pnlDriver.Controls.Add(this.lblDriverVersion);
            this.pnlDriver.Controls.Add(this.btnRefDriver);
            this.pnlDriver.Controls.Add(this.cbDriver);
            this.pnlDriver.Location = new System.Drawing.Point(223, 18);
            this.pnlDriver.Name = "pnlDriver";
            this.pnlDriver.Size = new System.Drawing.Size(487, 37);
            this.pnlDriver.TabIndex = 8;
            // 
            // lblDriverVersion
            // 
            this.lblDriverVersion.AutoSize = true;
            this.lblDriverVersion.Location = new System.Drawing.Point(274, 15);
            this.lblDriverVersion.Name = "lblDriverVersion";
            this.lblDriverVersion.Size = new System.Drawing.Size(42, 13);
            this.lblDriverVersion.TabIndex = 8;
            this.lblDriverVersion.Text = "Version";
            // 
            // btnRefDriver
            // 
            this.btnRefDriver.Image = ((System.Drawing.Image)(resources.GetObject("btnRefDriver.Image")));
            this.btnRefDriver.Location = new System.Drawing.Point(238, 12);
            this.btnRefDriver.Name = "btnRefDriver";
            this.btnRefDriver.Size = new System.Drawing.Size(21, 21);
            this.btnRefDriver.TabIndex = 7;
            this.btnRefDriver.UseVisualStyleBackColor = true;
            this.btnRefDriver.Click += new System.EventHandler(this.btnRefDriver_Click);
            // 
            // cbDriver
            // 
            this.cbDriver.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbDriver.FormattingEnabled = true;
            this.cbDriver.Location = new System.Drawing.Point(23, 13);
            this.cbDriver.Name = "cbDriver";
            this.cbDriver.Size = new System.Drawing.Size(201, 21);
            this.cbDriver.TabIndex = 6;
            this.cbDriver.SelectedIndexChanged += new System.EventHandler(this.cbDriver_SelectedIndexChanged);
            // 
            // chkAutoSend
            // 
            this.chkAutoSend.AutoSize = true;
            this.chkAutoSend.Location = new System.Drawing.Point(13, 69);
            this.chkAutoSend.Name = "chkAutoSend";
            this.chkAutoSend.Size = new System.Drawing.Size(165, 17);
            this.chkAutoSend.TabIndex = 22;
            this.chkAutoSend.Text = "Enter Key trigger Send button";
            this.chkAutoSend.UseVisualStyleBackColor = true;
            // 
            // chkPermanent
            // 
            this.chkPermanent.AutoSize = true;
            this.chkPermanent.Location = new System.Drawing.Point(13, 92);
            this.chkPermanent.Name = "chkPermanent";
            this.chkPermanent.Size = new System.Drawing.Size(134, 17);
            this.chkPermanent.TabIndex = 12;
            this.chkPermanent.Text = "Permanent Connection";
            this.chkPermanent.UseVisualStyleBackColor = true;
            this.chkPermanent.CheckedChanged += new System.EventHandler(this.chkPermanent_CheckedChanged);
            // 
            // panelSocket
            // 
            this.panelSocket.Controls.Add(this.btn_TCPIPRefresh);
            this.panelSocket.Controls.Add(this.cbTCPIP);
            this.panelSocket.Controls.Add(this.txtIP);
            this.panelSocket.Controls.Add(this.label2);
            this.panelSocket.Controls.Add(this.label3);
            this.panelSocket.Controls.Add(this.txtPort);
            this.panelSocket.Location = new System.Drawing.Point(220, 19);
            this.panelSocket.Name = "panelSocket";
            this.panelSocket.Size = new System.Drawing.Size(520, 36);
            this.panelSocket.TabIndex = 7;
            // 
            // btn_TCPIPRefresh
            // 
            this.btn_TCPIPRefresh.Image = ((System.Drawing.Image)(resources.GetObject("btn_TCPIPRefresh.Image")));
            this.btn_TCPIPRefresh.Location = new System.Drawing.Point(154, 13);
            this.btn_TCPIPRefresh.Name = "btn_TCPIPRefresh";
            this.btn_TCPIPRefresh.Size = new System.Drawing.Size(21, 21);
            this.btn_TCPIPRefresh.TabIndex = 7;
            this.btn_TCPIPRefresh.UseVisualStyleBackColor = true;
            this.btn_TCPIPRefresh.Click += new System.EventHandler(this.btn_TCPIPRefresh_Click);
            // 
            // cbTCPIP
            // 
            this.cbTCPIP.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbTCPIP.FormattingEnabled = true;
            this.cbTCPIP.Location = new System.Drawing.Point(26, 13);
            this.cbTCPIP.Name = "cbTCPIP";
            this.cbTCPIP.Size = new System.Drawing.Size(121, 21);
            this.cbTCPIP.TabIndex = 6;
            this.cbTCPIP.SelectedIndexChanged += new System.EventHandler(this.cbTCPIP_SelectedIndexChanged);
            // 
            // txtIP
            // 
            this.txtIP.Location = new System.Drawing.Point(207, 13);
            this.txtIP.Name = "txtIP";
            this.txtIP.Size = new System.Drawing.Size(100, 20);
            this.txtIP.TabIndex = 3;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(186, 17);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(17, 13);
            this.label2.TabIndex = 2;
            this.label2.Text = "IP";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(312, 17);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(26, 13);
            this.label3.TabIndex = 4;
            this.label3.Text = "Port";
            // 
            // txtPort
            // 
            this.txtPort.Location = new System.Drawing.Point(342, 14);
            this.txtPort.Name = "txtPort";
            this.txtPort.Size = new System.Drawing.Size(53, 20);
            this.txtPort.TabIndex = 5;
            this.txtPort.Text = "9100";
            // 
            // groupBox1
            // 
            this.groupBox1.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
            this.groupBox1.Controls.Add(this.btnLoadFile);
            this.groupBox1.Controls.Add(this.txtSend);
            this.groupBox1.Controls.Add(this.btnClear);
            this.groupBox1.Controls.Add(this.btnQuery);
            this.groupBox1.Controls.Add(this.btnSend);
            this.groupBox1.Location = new System.Drawing.Point(-1, 141);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(740, 167);
            this.groupBox1.TabIndex = 3;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Send Data";
            // 
            // btnLoadFile
            // 
            this.btnLoadFile.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnLoadFile.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnLoadFile.Location = new System.Drawing.Point(576, 19);
            this.btnLoadFile.Name = "btnLoadFile";
            this.btnLoadFile.Size = new System.Drawing.Size(134, 25);
            this.btnLoadFile.TabIndex = 23;
            this.btnLoadFile.Text = "Load File";
            this.btnLoadFile.UseVisualStyleBackColor = true;
            this.btnLoadFile.Click += new System.EventHandler(this.btnLoadFile_Click);
            // 
            // txtSend
            // 
            this.txtSend.Font = new System.Drawing.Font("Courier New", 8.25F);
            this.txtSend.Location = new System.Drawing.Point(13, 19);
            this.txtSend.MaxLength = 0;
            this.txtSend.Name = "txtSend";
            this.txtSend.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.Vertical;
            this.txtSend.Size = new System.Drawing.Size(536, 125);
            this.txtSend.TabIndex = 0;
            this.txtSend.Text = "";
            this.txtSend.KeyUp += new System.Windows.Forms.KeyEventHandler(this.txtSend_KeyUp);
            // 
            // btnClear
            // 
            this.btnClear.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F);
            this.btnClear.Location = new System.Drawing.Point(576, 119);
            this.btnClear.Margin = new System.Windows.Forms.Padding(0);
            this.btnClear.Name = "btnClear";
            this.btnClear.Size = new System.Drawing.Size(134, 25);
            this.btnClear.TabIndex = 21;
            this.btnClear.Text = "Clear All";
            this.btnClear.UseVisualStyleBackColor = true;
            this.btnClear.Click += new System.EventHandler(this.btnClearSend_Click);
            // 
            // btnQuery
            // 
            this.btnQuery.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnQuery.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnQuery.Location = new System.Drawing.Point(576, 84);
            this.btnQuery.Name = "btnQuery";
            this.btnQuery.Size = new System.Drawing.Size(134, 25);
            this.btnQuery.TabIndex = 10;
            this.btnQuery.Text = "Query";
            this.btnQuery.UseVisualStyleBackColor = true;
            this.btnQuery.Click += new System.EventHandler(this.btnQuery_Click);
            // 
            // btnSend
            // 
            this.btnSend.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnSend.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnSend.Location = new System.Drawing.Point(576, 51);
            this.btnSend.Name = "btnSend";
            this.btnSend.Size = new System.Drawing.Size(134, 25);
            this.btnSend.TabIndex = 7;
            this.btnSend.Text = "Send";
            this.btnSend.UseVisualStyleBackColor = true;
            this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
            // 
            // notifyIcon
            // 
            this.notifyIcon.BalloonTipIcon = System.Windows.Forms.ToolTipIcon.Info;
            this.notifyIcon.BalloonTipText = "Running in system tray";
            this.notifyIcon.BalloonTipTitle = "SATO Printer API - Sample Apps";
            this.notifyIcon.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon.Icon")));
            this.notifyIcon.Text = "SATO Printer API - Sample Apps";
            this.notifyIcon.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.notifyIcon_MouseDoubleClick);
            // 
            // SampleForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(739, 493);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.groupBox4);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "SampleForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Sample Form for SATO Printer API";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.SampleForm_FormClosing);
            this.Load += new System.EventHandler(this.SampleForm_Load);
            this.Shown += new System.EventHandler(this.SampleForm_Shown);
            this.Resize += new System.EventHandler(this.SampleForm_Resize);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox6.ResumeLayout(false);
            this.groupBox6.PerformLayout();
            this.groupBox4.ResumeLayout(false);
            this.groupBox4.PerformLayout();
            this.panelUSB.ResumeLayout(false);
            this.pnlDriver.ResumeLayout(false);
            this.pnlDriver.PerformLayout();
            this.panelSocket.ResumeLayout(false);
            this.panelSocket.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.GroupBox groupBox6;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.TextBox txtTotalRecvBytes;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox cbInterfaces;
        private System.Windows.Forms.Panel panelSocket;
        private System.Windows.Forms.Button btn_TCPIPRefresh;
        private System.Windows.Forms.ComboBox cbTCPIP;
        private System.Windows.Forms.TextBox txtIP;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtPort;
        private System.Windows.Forms.Panel panelUSB;
        private System.Windows.Forms.Button btnRefresh;
        private System.Windows.Forms.ComboBox cbUSBPorts;
        private System.Windows.Forms.Panel pnlDriver;
        private System.Windows.Forms.Button btnRefDriver;
        private System.Windows.Forms.ComboBox cbDriver;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button btnClear;
        private System.Windows.Forms.Button btnQuery;
        private System.Windows.Forms.Button btnSend;
        private System.Windows.Forms.TextBox rtxtRecv;
        private System.Windows.Forms.CheckBox chkPermanent;
        private System.Windows.Forms.Label lblDriverVersion;
        private System.Windows.Forms.RichTextBox txtSend;
        private System.Windows.Forms.CheckBox chkSocketServer;
        private System.Windows.Forms.CheckBox chkAutoSend;
        private System.Windows.Forms.Button btnLoadFile;
        private System.Windows.Forms.NotifyIcon notifyIcon;
        private System.Windows.Forms.TextBox txtTimeout;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btnExport;
    }
}