package net.earthcomputer.playinggod.client;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public interface IKeyboardListener {

	void onKeyPress(char typedChar, int keyCode, boolean repeat);

	void onKeyRelease(char typedChar, int keyCode, boolean repeat);

}
