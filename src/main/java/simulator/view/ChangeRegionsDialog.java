package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

  private DefaultComboBoxModel<String> regionsModel;
  private DefaultComboBoxModel<String> fromRowModel;
  private DefaultComboBoxModel<String> toRowModel;
  private DefaultComboBoxModel<String> fromColModel;
  private DefaultComboBoxModel<String> toColModel;

  private DefaultTableModel dataTableModel;
  private Controller ctrl;
  private List<JSONObject> regionsInfo;

  private String[] headers = { "Key", "Value", "Description" };

  // TODO en caso de ser necesario, añadir los atributos aquí…
  private int status;
  ChangeRegionsDialog(Controller ctrl) {
    super((Frame)null, true);
    this.ctrl = ctrl;
    initGUI();
    ctrl.addObserver(this);
    // TODO registrar this como observer;
  }

  private void initGUI() {
    setTitle("Change Regions");
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    setContentPane(mainPanel);

    // TODO crea varios paneles para organizar los componentes visuales en el
    //      dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
    //      uno para la tabla, uno para los combobox, y uno para los botones.
    JPanel helpPanel = new JPanel();
		helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
		mainPanel.add(helpPanel);

    JPanel tablePanel = new JPanel();
    tablePanel.setLayout(new BorderLayout());
    mainPanel.add(tablePanel);

    JPanel comboboxPanel = new JPanel();
    mainPanel.add(comboboxPanel);

    JPanel buttonsPanel = new JPanel();
    mainPanel.add(buttonsPanel);

    // TODO crear el texto de ayuda que aparece en la parte superior del diálogo y
    //      añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
    JLabel helpText = new JLabel("<html>Select a region type, the rows/cols interval, and provide values for the parameters in the Value column (default values are used for parameters with no value).</html>");
		helpText.setAlignmentX(LEFT_ALIGNMENT);
		helpPanel.add(helpText);

    // this.regionsInfo se usará para establecer la información en la tabla
    this.regionsInfo = Main.regionFactory.getInfo();

    // this.dataTableModel es un modelo de tabla que incluye todos los parámetros de
    // la region
    this.dataTableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        // TODO hacer editable solo la columna 1
        return column == 1;
      }
    };
    this.dataTableModel.setColumnIdentifiers(this.headers);

    // TODO crear un JTable que use dataTableModel, y añadirlo al diálogo
    JTable dataTable = new JTable(this.dataTableModel);
    JScrollPane tableScroll = new JScrollPane(dataTable);
    tablePanel.add(tableScroll, BorderLayout.CENTER);

    // this.regionsModel es un modelo de combobox que incluye los tipos de regiones
    this.regionsModel = new DefaultComboBoxModel<>();

    // TODO añadir la descripción de todas las regiones a regionsModel. Para eso
    //      usa la clave “desc” o “type” de los JSONObject en regionsInfo,
    //      ya que estos nos dan información sobre lo que puede crear la factoría.
    for (JSONObject o: this.regionsInfo){
      String s;
      if(o.has("desc")){
        s = o.getString("desc");
      }
      else{
        s = o.getString("type");
      }
      this.regionsModel.addElement(s);
    }

    // TODO crear un combobox que use regionsModel y añadirlo al diálogo.
    JLabel regionChooserText = new JLabel();
		regionChooserText.setText("Region type: ");
    JComboBox<String> regionsComboBox = new JComboBox<>(this.regionsModel);
    regionsComboBox.setSelectedIndex(0);
    regionsComboBox.addActionListener(e -> updateRegionDataTable(regionsComboBox)); //cuando cambia la selección se refreca la tabla
    comboboxPanel.add(regionChooserText);
    comboboxPanel.add(regionsComboBox);

    // TODO crear 4 modelos de combobox para this.fromRowModel, this.toRowModel,
    //      this.fromColModel y this.toColModel.
    this.fromRowModel = new DefaultComboBoxModel<>();
    this.toRowModel = new DefaultComboBoxModel<>();
    this.fromColModel = new DefaultComboBoxModel<>();
    this.toColModel = new DefaultComboBoxModel<>();


    // TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
    JComboBox<String> fromRowCombo = new JComboBox<>(this.fromRowModel);
    JComboBox<String> toRowCombo = new JComboBox<>(this.toRowModel);
    JComboBox<String> fromColCombo = new JComboBox<>(this.fromColModel);
    JComboBox<String> toColCombo = new JComboBox<>(this.toColModel);
    
    JLabel rowChooserLabel = new JLabel();
		rowChooserLabel.setText("Row from/to: ");
    comboboxPanel.add(rowChooserLabel);
    comboboxPanel.add(fromRowCombo);
    comboboxPanel.add(toRowCombo);
    
    JLabel colChooserLabel = new JLabel();
		colChooserLabel.setText("Col from/to: ");
    comboboxPanel.add(colChooserLabel);
    comboboxPanel.add(fromColCombo);
    comboboxPanel.add(toColCombo);


    // TODO crear los botones OK y Cancel y añadirlos al diálogo.
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener((e) -> {
      status = 0;
      setVisible(false);
		});

    //REVISAR
    okButton.addActionListener((e) -> {
            String type = regionsInfo.get(regionsComboBox.getSelectedIndex()).getString("type"); //COJO EL TIPO
            JSONObject region = new JSONObject();

            JSONArray row = new JSONArray();
            JSONArray col = new JSONArray();
            
            row.put(fromRowCombo.getSelectedIndex());//INICIO RANGO ROW
            row.put(toRowCombo.getSelectedIndex());//FIN RANGO ROW
            
            col.put(fromColCombo.getSelectedIndex());//INICIO RANGO col
            col.put(toColCombo.getSelectedIndex());//FIN RANGO col
            
            if(fromRowCombo.getSelectedIndex() > toRowCombo.getSelectedIndex()){
            	ViewUtils.showErrorMsg("Rango inválido");
            	return;
            }
            if(fromColCombo.getSelectedIndex() > toColCombo.getSelectedIndex()){
            	ViewUtils.showErrorMsg("Rango inválido");
            	return;
            }
            
	        region.put("row", row);
	        region.put("col", col);

	        JSONObject spec = new JSONObject();

	        // INSERTAMOS TIPO
	        spec.put("type", type);

	        JSONObject data = new JSONObject();
	        String k;
	        Object v;

	        for (int fila = 0; fila < dataTableModel.getRowCount(); fila++) { //FILAS DE TABLA CHANGEREGIONS (VALUE)
	            k = (String) dataTableModel.getValueAt(fila, 0);
	            v = dataTableModel.getValueAt(fila, 1);
	            data.put(k, v);
	        }

	        spec.put("data", data);
	        region.put("spec", spec);

	        JSONObject regiones = new JSONObject();
	        JSONArray arrayRegiones = new JSONArray();
	        arrayRegiones.put(region);
	        regiones.put("regions",arrayRegiones);
	        ctrl.setRegions(regiones);
	        dispose();
		});

    buttonsPanel.add(okButton);
    buttonsPanel.add(cancelButton);

    setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
    pack();
    setResizable(false);
    setVisible(false);
  }

  public void open(Frame parent) {
    setLocation(
      parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
      parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
    pack();
    setVisible(true);
  }

  @Override
  public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onRegister'");
  }

  @Override
  public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onReset'");
  }

  @Override
  public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onAnimalAdded'");
  }

  @Override
  public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onRegionSet'");
  }

  @Override
  public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onAdvance'");
  }

  // TODO el resto de métodos van aquí…
  public void updateRegionDataTable(JComboBox<String> regionsComboBox){
    dataTableModel.setRowCount(0); 
		int index = regionsComboBox.getSelectedIndex();

		JSONObject region = regionsInfo.get(index);
		if (region.has("data")) {
			JSONObject data = region.getJSONObject("data");
			Iterator<String> keys = data.keys();
			String k;
			while (keys.hasNext()) {
				k = keys.next();
				String[] fila = {k, "", data.getString(k)}; //key, value(vacío), descrption
				dataTableModel.addRow(fila);
			}
		}

  }
}
