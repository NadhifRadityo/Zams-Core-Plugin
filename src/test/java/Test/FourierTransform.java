package Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.WindowHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow;

public class FourierTransform {
	private XYDataset createDataset() {

        XYSeries series = new XYSeries("Sound");
//        series.add(18, 567);
//        series.add(20, 612);
//        series.add(25, 800);
//        series.add(30, 980);
//        series.add(40, 1410);
//        series.add(50, 2350);
        Map<Integer, Long> datas = getSound("D:/Tio/MC Server Build/3_bps.wav");
        for(Entry<Integer, Long> data : datas.entrySet()){
        	series.add(data.getKey(), data.getValue());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }
	
	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
                "Fourier Transform", 
                "Time", 
                "Intensity", 
                dataset, 
                PlotOrientation.VERTICAL,
                true, 
                true, 
                false 
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Fourier Transform",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }
	
	public FourierTransform() {
		FrameWindow window = new FrameWindow("Fourier Transform", new WindowHelper());
//		window.setSize(700, 500);
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		window.display();
		
		JPanel audioGraphPanel = new JPanel();
		audioGraphPanel.setBackground(Color.WHITE);
		audioGraphPanel.setOpaque(true);
		window.add(audioGraphPanel);
		
		XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setSize(new Dimension(window.getWidth(), 420));
        chartPanel.setPreferredSize(new Dimension(window.getWidth(), 420));
        audioGraphPanel.add(chartPanel);
		
		float cycles = 3.00f;
		float per = 1 / cycles;
		
		JPanel circle = new JPanel();
		audioGraphPanel.add(circle);
		circle.setSize(50, 50);
//		circle.setBackground(Color.BLUE);
//		circle.setOpaque(true);
		
		window.display();
	}
	
	private static long extendSign(long temp, int bitsPerSample) {
        int extensionBits = 64 - bitsPerSample;
        return (temp << extensionBits) >> extensionBits;
    }
	
	private Map<Integer, Long> getSound(String sound) {
		Map<Integer, Long> list = new HashMap<Integer, Long>();
		
		AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(sound));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		AudioFormat format = audioIn.getFormat();
		TargetDataLine tdl = null;
        
        try {
            tdl = AudioSystem.getTargetDataLine(format);
			tdl.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        
        tdl.start();
        if (!tdl.isOpen()) {
            System.exit(1);         
        } 
        byte[] data = new byte[(int) format.getSampleRate() * 10];
        int read = tdl.read(data, 0, (int) format.getSampleRate() * 10);
        if (read > 0) {
            for (int i = 0; i < read-1; i = i + 2) {
                long val = ((data[i] & 0xffL) << 8L) | (data[i + 1] & 0xffL);
                long valf = extendSign(val, 16);
                System.out.println(i + "\t" + valf);
//                list.add(valf);
                list.put(i, valf);
            }
        }
        tdl.close();
//        return list.toArray(new Long[list.size()]);
        return list;
	}
	
	public static void main(String[] args) throws LineUnavailableException {
		SwingUtilities.invokeLater(() -> {
            new FourierTransform();
        });
		
	}
}
