package com.example.imc;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public EditText poidsValue;
	public EditText tailleValue;
	public TextView result;

	public Button calcul;
	public Button raz;

	public RadioGroup group;

	public CheckBox mega;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initListener();
	}

	public void initView(){
		poidsValue = (EditText) findViewById(R.id.poidsValue);
		tailleValue = (EditText) findViewById(R.id.tailleValue);
		result = (TextView) findViewById(R.id.result);
		calcul = (Button) findViewById(R.id.calcul);
		raz = (Button) findViewById(R.id.raz);
		group = (RadioGroup) findViewById(R.id.group);
		mega = (CheckBox) findViewById(R.id.mega);
	}

	public void initListener(){
		calcul.setOnClickListener(new CalculListener());
		raz.setOnClickListener(new RazListener());
		poidsValue.addTextChangedListener(textWatcher);
		tailleValue.addTextChangedListener(textWatcher);
	}

	/**
	 * Calcul l'indice de masse corporelle
	 * @param poids en kg
	 * @param taille en metres
	 * @return
	 */
	public float calculateIMC(float poids,float taille){
		return poids / (taille * taille);
	}

	public void doRaz(){
		poidsValue.getText().clear();
		tailleValue.getText().clear();
		result.setText(R.string.infoLabel);
	}

	public float cmToMetre(float cmValue){
		return cmValue / 100;
	}

	public void showWarningMessage(){
		CharSequence warningMessage = new String(getResources().getString(R.string.warningMessage));
		Toast warning = Toast.makeText(getApplicationContext(), warningMessage, Toast.LENGTH_SHORT);
		warning.show();
	}

	public void showErrorMessage(){
		CharSequence errorMessage = new String(getResources().getString(R.string.errMessage));
		Toast err = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
		err.show();
	}

	public class CalculListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String poidsTextValue = poidsValue.getText().toString();
			String tailleTextValue = tailleValue.getText().toString();

			if(!isValid(poidsTextValue) || !isValid(tailleTextValue)){
				showWarningMessage();
				return;
			}

			float poids = Float.parseFloat(poidsTextValue);
			float taille = Float.parseFloat(tailleTextValue);

			if(taille == 0){
				showErrorMessage();
				return;
			}
			if(group.getCheckedRadioButtonId() == R.id.radioCentimetre){
				taille = cmToMetre(taille);
			}
			float imc = calculateIMC(poids, taille);
			String resultatIMC = Float.toString(imc);
			if(mega.isChecked()){
				resultatIMC = resultatIMC + " ("+getIMCInterpretation(imc)+").";
			}
			result.setText(resultatIMC);
		}

	}

	public class RazListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			doRaz();
		}

	}

	/**
	 * - de 16.5	famine
	 * 16.5 à 18.5	maigreur
	 * 18.5 à 25	corpulence normale
	 * 25 à 30	surpoids
	 * 30 à 35	obésité modérée
	 * 35 à 40	obésité sévère
	 * + de 40	obésité morbide ou massive
	 * @param imc
	 * @return
	 */
	public String getIMCInterpretation(float imc){
		if(imc < 16.5){
			return "famine";
		}else if(imc >= 16.5 && imc <= 18.5){
			return "maigreur";
		}else if(imc > 18.5 && imc <= 25.0){
			return "corpulence normale";
		}else if(imc > 25.0 && imc <= 30.0){
			return "surpoids";
		}else if(imc > 30.0 && imc <= 35.0){
			return "obésité modérée";
		}else if(imc > 35.0 && imc <= 40.0){
			return "obésité sévère";
		}else if(imc > 40.0){
			return "obésité morbide ou massive";
		}else return "imc unknow";
	}

	public boolean isValid(String textValue){
		return textValue != null && !textValue.equals("");
	}
	
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			result.setText(R.string.infoLabel);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			return;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			return;
		}
	};

}
