package me.lumpchen.xafp.tool.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.lumpchen.xafp.Document;
import me.lumpchen.xafp.PrintFile;

public class NavigatorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	private JList<String> docIndexList;
	private JList<String> pageIndexList;

	private PrintFile pf;
	private PageCanvas canvas;

	public NavigatorPanel() {
		super();
		this.setLayout(new BorderLayout());

		Border etchedLoweredBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		this.docIndexList = new JList<String>();
		this.docIndexList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Border titledBorderAtTop = BorderFactory.createTitledBorder(etchedLoweredBorder, "Document:", TitledBorder.LEFT,
				TitledBorder.TOP);
		JScrollPane docIndexScrollPane = new JScrollPane();
		docIndexScrollPane.setBorder(titledBorderAtTop);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		this.docIndexList.setBorder(lineBorder);
		docIndexScrollPane.setViewportView(this.docIndexList);

		this.pageIndexList = new JList<String>();
		this.pageIndexList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane pageIndexScrollPane = new JScrollPane();
		titledBorderAtTop = BorderFactory.createTitledBorder(etchedLoweredBorder, "Page:", TitledBorder.LEFT,
				TitledBorder.TOP);
		pageIndexScrollPane.setBorder(titledBorderAtTop);
		this.pageIndexList.setBorder(lineBorder);
		pageIndexScrollPane.setViewportView(this.pageIndexList);

		JPanel indexPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(indexPanel, BoxLayout.Y_AXIS);
		indexPanel.setLayout(boxLayout);

		indexPanel.add(docIndexScrollPane);
		indexPanel.add(pageIndexScrollPane);

		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.tabbedPane.add("Index", indexPanel);
		this.tabbedPane.add("TLE", new JPanel());

		this.add(tabbedPane, BorderLayout.CENTER);
	}

	private void updateDocumentIndex() {
		List<Document> docs = this.pf.getDocuments();
		String[] elements = new String[docs.size()];
		for (int i = 0; i < docs.size(); i++) {
			elements[i] = (i + 1) + "";
		}
		this.docIndexList.setListData(elements);
		this.docIndexList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				int index = docIndexList.getSelectedIndex();
				if (index < 0) {
					return;
				}
				updatePageIndex(pf.getDocuments().get(index));
			}
		});
	}

	private void updatePageIndex(Document doc) {
		int pageCount = doc.getPageCount();
		String[] elements = new String[pageCount];
		for (int i = 0; i < pageCount; i++) {
			elements[i] = (i + 1) + "";
		}
		this.pageIndexList.setListData(elements);
		this.pageIndexList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				int pageIndex = pageIndexList.getSelectedIndex();
				if (pageIndex < 0) {
					return;
				}
				int docIndex = docIndexList.getSelectedIndex();
				canvas.updatePage(pf, docIndex, pageIndex);
			}
		});
	}

	public void updateDocument(PrintFile pf, PageCanvas canvas) {
		this.pf = pf;
		this.canvas = canvas;
		this.docIndexList.setListData(new String[0]);
		this.pageIndexList.setListData(new String[0]);
		this.updateDocumentIndex(); 
	}
	
	public void closeFile() {
		this.docIndexList.setListData(new String[0]);
		this.pageIndexList.setListData(new String[0]);
	}
}
