package com.taut.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.taut.game.levels.test.screens.TestScreen;

public class MainMenu extends ScreenAdapter implements InputProcessor {

	Taut game;
	int selectedButton;
	
	float selectedBlinkTime = 0.0f;
	final float BLINK_DELTA = .5f;
	boolean blinkOn = false;
	
	BitmapFont font;
	List<TextButton> textButtons;
	TextButtonStyle buttonStyle;
	SpriteBatch batch;
	Stage stage;
	
	protected static class ButtonText
	{
		public static String continueGame = "Continue";
		public static String newGame = "New Game";
		public static String options = "Options";
		public static String quit = "Quit";
	}
	
	public MainMenu(Taut game)
	{
		this.game = game;
	}
	
	@Override
	public void show()
	{
		font = GlobalData.getMainFont();
		buttonStyle = getButtonStyle();
		textButtons = getButtons();
		selectedButton = textButtons.size()-1;
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(this);
	}
	
	private void preRender(float delta)
	{
		float padding = Gdx.graphics.getHeight()/16f;	
		float pastButtonTop = 0.0f;

		for(int x = 0; x < textButtons.size(); x++)
		{
			TextButton currentButton = textButtons.get(x);
			
			
			if(x == selectedButton)
			{
				currentButton.getStyle().fontColor = new Color(1f, 1f, 1f, 1f);
			}else
			{
				currentButton.getStyle().fontColor = new Color(1f, 1f, 1f, .7f);
			}
			
			
			// scale it such that at 680x480 or any of same aspect ratio it will
			// scale buttons to be aligned perfectly
			// TODO: automatically fix for all aspect ratios
			currentButton.setSize(Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/9.6f); 
			
			currentButton.setX((Gdx.graphics.getWidth()/2f) - currentButton.getWidth()/2f); // set to middle
			currentButton.setY(pastButtonTop + padding);
			pastButtonTop = currentButton.getY() + currentButton.getHeight();
		}
	}
	
	@Override
	public void render(float delta)
	{
		preRender(delta);
		
		batch.begin();
		renderLogo(delta);
		renderButtons(delta);
		batch.end();
	}
	
	private void renderButtons(float delta)
	{
		for(int x = 0; x < textButtons.size(); x++)
		{
			textButtons.get(x).draw(batch, 1f);
		}
	}
	
	
	// TODO: implement when logo available
	private void renderLogo(float delta)
	{
		
	}
	
	private List<TextButton> getButtons()
	{
		List<TextButton> buttons = new ArrayList<TextButton>();
				
		buttons.add(new TextButton(ButtonText.quit, new TextButtonStyle(buttonStyle)));
		buttons.add(new TextButton(ButtonText.options, new TextButtonStyle(buttonStyle)));
		buttons.add(new TextButton(ButtonText.newGame, new TextButtonStyle(buttonStyle)));
		buttons.add(new TextButton(ButtonText.continueGame, new TextButtonStyle(buttonStyle)));
		return buttons;
	}
	
	private TextButtonStyle getButtonStyle()
	{
		TextButtonStyle style = new TextButtonStyle();
		style.font = font;
		Texture woodTexture = new Texture(Gdx.files.internal("wood-tile.jpg"));
		style.up = new TextureRegionDrawable(new TextureRegion(woodTexture));
		return style;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.UP)
		{
			selectedButton++;
			selectedButton %= textButtons.size(); 
		}else if(keycode == Input.Keys.DOWN)
		{
			selectedButton--;
			if(selectedButton < 0)
				selectedButton = textButtons.size()-1;
		}else if(keycode == Input.Keys.ENTER)
		{
			handleButtonPress(textButtons.get(selectedButton));
		}
		
		return false;
	}

	private void handleButtonPress(TextButton button)
	{
		String buttonText = button.getText().toString();
		if(buttonText.equals(ButtonText.continueGame))
		{
			game.setScreen(new TestScreen(game, "Test", "Test"));
			batch.dispose();
		}else if(buttonText.equals(ButtonText.newGame))
		{
			game.setScreen(new TestScreen(game, "Test", "Test"));
		}else if(buttonText.equals(ButtonText.options))
		{
			//TODO: implement
		}else if(buttonText.equals(ButtonText.quit))
		{
			Gdx.app.exit();
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
