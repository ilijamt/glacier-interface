package com.matoski.glacier.util;

/**
 * Progress bar for upload to amazon glacier
 * 
 * @author ilijamt
 */
public class AmazonGlacierProgressBar {

    /**
     * The string to be display on the screen
     */
    private StringBuilder progress;

    /**
     * Set total items
     */
    private long total;

    /**
     * Set transferred items
     */
    private long transferred;

    /**
     * initialize progress bar properties.
     */
    public AmazonGlacierProgressBar() {
	init();
    }

    public void init() {
	this.progress = new StringBuilder(60);
	this.setTotal(0);
	this.setTransferred(0);
    }

    /**
     * called whenever the progress bar needs to be updated. that is whenever
     * progress was made.
     *
     * @param done
     *            an int representing the work done so far
     * @param total
     *            an int representing the total work
     */
    public void process(int done, int total) {
	// contains the characters that create the spinning wait symbol
	char[] workchars = { '|', '/', '-', '\\' };
	/**
	 * it is the progress bar format. let's have a closer look: - \r is the
	 * carriage return, in other words it moves the "cursor" to the first
	 * position on the same line - %3d is a 3 digit decimal integer - %% is
	 * the literal % character - %s is a string, particularly the "###...#"
	 * symbols - %c is a character, particularly the spinning wait symbol
	 */
	String format = "\r%3d%% %s %c";

	// calculates how much work units per cent have been completed
	int percent = (++done * 100) / total;
	// total number of # to be appended
	int extrachars = (percent / 2) - this.progress.length();

	// append the # to the progress bar
	while (extrachars-- > 0) {
	    progress.append('#');
	}

	// updates/redraws the progress bar
	System.out.printf(format, percent, progress, workchars[done % workchars.length]);

	/**
	 * if the work have been completed it forces the output to be written,
	 * changes line and resets itself so it can be reused.
	 */
	if (done == total) {
	    System.out.flush();
	    System.out.println();
	    init();
	}
    }

    /**
     * Set total progress
     * 
     * @param total
     */
    public void setTotal(long total) {
	this.total = total;
    }

    /**
     * Set transfered progress
     * 
     * @param transfered
     */
    public void setTransferred(long transfered) {
	this.transferred = transfered;
    }

    /**
     * Update the console
     * 
     * @param transferred
     */
    public void update(long transferred) {

	int done = 0;
	int total = 100;
	this.transferred += transferred;

	done = (int) ((this.transferred / this.total) * 100.0);

	if (done >= total) {
	    done--;
	}

	process(done, total);

    }
}
