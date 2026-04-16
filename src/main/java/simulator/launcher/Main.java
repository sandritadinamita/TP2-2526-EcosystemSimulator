package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.DefaultRegionBuilder;
import simulator.factories.DynamicSupplyRegionBuilder;
import simulator.factories.Factory;
import simulator.factories.SelectClosestBuilder;
import simulator.factories.SelectFirstBuilder;
import simulator.factories.SelectYoungestBuilder;
import simulator.factories.SheepBuilder;
import simulator.factories.WolfBuilder;
import simulator.misc.Utils;
import simulator.model.Animal;
import simulator.model.Constantes;
import simulator.model.Region;
import simulator.model.SelectionStrategy;
import simulator.model.Simulator;
import simulator.view.MainWindow;

public class Main {
  public static Factory<SelectionStrategy> selectionStrategyFactory;
	public static Factory<Animal> animalsFactory;
	public static Factory<Region> regionFactory; // aqui no se si los que se tienen que poner en publico son estos y el delta time o alguno mas


  private enum ExecMode {
    BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

    private String tag;
    private String desc;

    private ExecMode(String modeTag, String modeDesc) {
      tag = modeTag;
      desc = modeDesc;
    }

    public String getTag() {
      return tag;
    }

    public String getDesc() {
      return desc;
    }
  }

  // default values for some parameters
  //
  private final static Double DEFAULT_TIME = 10.0; // in seconds
  private final static Double DEFAULT_DELTATIME = 0.03;

  // some attributes to stores values corresponding to command-line parameters
  //
  private static Double time = null;
  public static Double deltaTime = null;
  private static String inFile = null;
  private static String outFile = null;
  private static boolean viewer = false;

  private static ExecMode mode = ExecMode.BATCH;

  private static void parseArgs(String[] args) {

    // define the valid command line options
    //
    Options cmdLineOptions = buildOptions();

    // parse the command line as provided in args
    //
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine line = parser.parse(cmdLineOptions, args);
      parseHelpOption(line, cmdLineOptions);
      parseInFileOption(line);
      parseOutFileOption(line);
      parseTimeOption(line);
      parseDeltaTimeOption(line);
      parseSimViewerOption(line, cmdLineOptions);
      parseModeOption(line);

      // if there are some remaining arguments, then something wrong is
      // provided in the command line!
      //
      String[] remaining = line.getArgs();
      if (remaining.length > 0) {
        String error = "Illegal arguments:";
        for (String o : remaining)
          error += (" " + o);
        throw new ParseException(error);
      }

    } catch (ParseException e) {
      System.err.println(e.getLocalizedMessage());
      System.exit(1);
    }

  }

  private static Options buildOptions() {
    Options cmdLineOptions = new Options();

    // help
    cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

    // input file
    cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("A configuration file.").build());
    cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written.").build());

    // steps
    cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
      .desc("A double representing actual time, in seconds, per simulation step. Default value: "
        + DEFAULT_DELTATIME + ".")
      .build());

    cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
      .desc("An real number representing the total simulation time in seconds. Default value: "
        + DEFAULT_TIME + ".")
      .build());

    cmdLineOptions.addOption(Option.builder("sv").longOpt("simple-viewer").desc("Show the viewer window in console mode.").build());


    return cmdLineOptions;
  }

  private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
    if (line.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
      System.exit(0);
    }
  }

  private static void parseSimViewerOption(CommandLine line, Options cmdLineOptions) {
    viewer = line.hasOption("sv");;
  }

  private static void parseInFileOption(CommandLine line) throws ParseException {
    inFile = line.getOptionValue("i");
    if (mode == ExecMode.BATCH && inFile == null) {
      throw new ParseException("In batch mode an input configuration file is required");
    }
  }

  private static void parseOutFileOption(CommandLine line) throws ParseException {
    outFile = line.getOptionValue("o");
    if (mode == ExecMode.BATCH && outFile == null) {
      throw new ParseException("In batch mode an output configuration file is required");
    }
  }

  private static void parseTimeOption(CommandLine line) throws ParseException {
    String t = line.getOptionValue("t", DEFAULT_TIME.toString());
    try {
      time = Double.parseDouble(t);
      assert (time >= 0);
    } catch (Exception e) {
      throw new ParseException("Invalid value for time: " + t);
    }
  }

  private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
    String dt = line.getOptionValue("dt", DEFAULT_DELTATIME.toString());
    try {
      deltaTime = Double.parseDouble(dt);
      assert (deltaTime >= 0);
    } catch (Exception e) {
      throw new ParseException("Invalid value for delta_time: " + dt);
    }
  }
  private static void parseModeOption(CommandLine line) throws ParseException {
    String m = line.getOptionValue("m", "gui");
    if ( m == "batch") {
      mode = ExecMode.BATCH;
    }
  }

  private static void initFactories() {
    //estrategias
    List<Builder<SelectionStrategy>> selectionStrategyBuilders = new ArrayList<>();
    selectionStrategyBuilders.add(new SelectFirstBuilder());
    selectionStrategyBuilders.add(new SelectClosestBuilder());
    selectionStrategyBuilders.add(new SelectFirstBuilder());
    selectionStrategyBuilders.add(new SelectYoungestBuilder());
    selectionStrategyFactory = new BuilderBasedFactory<SelectionStrategy>(selectionStrategyBuilders);
    //animales 
    List<Builder<Animal>> animalsBuilders = new ArrayList<>();
    animalsBuilders.add(new WolfBuilder(selectionStrategyFactory));
    animalsBuilders.add(new SheepBuilder(selectionStrategyFactory));
    animalsFactory = new BuilderBasedFactory<Animal>(animalsBuilders);
    //regiones
    List<Builder<Region>> regionsBuilders = new ArrayList<>();
    regionsBuilders.add(new DefaultRegionBuilder());
    regionsBuilders.add(new DynamicSupplyRegionBuilder());
    regionFactory = new BuilderBasedFactory<Region>(regionsBuilders);
  }

  private static JSONObject loadJSONFile(InputStream in) {
    return new JSONObject(new JSONTokener(in));
  }


  private static void start_batch_mode() throws Exception {
    InputStream is = new FileInputStream(new File(inFile));
    OutputStream os = new FileOutputStream(new File(outFile));
    JSONObject inputData = loadJSONFile(is);
    int w = inputData.getInt("width");
    int h = inputData.getInt("height");
    int c = inputData.getInt("rows");
    int e = inputData.getInt("cols");
    Simulator sim = new Simulator(e, c, w, h, animalsFactory, regionFactory);
    Controller control = new Controller(sim);
    control.loadData(inputData);
    control.run(time, deltaTime, viewer, os);
    os.close();
  }

  private static void start_GUI_mode() throws Exception {
    Controller ctrl;
    if(inFile != null){
      InputStream is = new FileInputStream(new File(inFile));
      JSONObject inputData = loadJSONFile(is);
      int w = inputData.getInt("width");
      int h = inputData.getInt("height");
      int c = inputData.getInt("rows");
      int e = inputData.getInt("cols");
      Simulator sim = new Simulator(e, c, w, h, animalsFactory, regionFactory);
      ctrl = new Controller(sim);
      ctrl.loadData(inputData);
    }
    else{
      Simulator sim = new Simulator(Constantes.DEFAULT_WIDTH, Constantes.DEFAULT_HEIGHT, Constantes.DEFAULT_ROWS, Constantes.DEFAULT_COLS, animalsFactory, regionFactory);
      ctrl = new Controller(sim);
    }
    SwingUtilities.invokeAndWait(() -> new MainWindow(ctrl));
  }

  private static void start(String[] args) throws Exception {
    initFactories();
    parseArgs(args);
    switch (mode) {
      case BATCH:
        start_batch_mode();
        break;
      case GUI:
        start_GUI_mode();
        break;
    }
  }

  
  public static void main(String[] args) {
    Utils.RAND.setSeed(2147483647l);
    try {
      start(args);
    } catch (Exception e) {
      System.err.println("Something went wrong ...");
      System.err.println();
      e.printStackTrace();
    }
  }
}
