/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfRegrowth extends Wand {

	{
		name = "Wand of Regrowth";
		image = ItemSpriteSheet.WAND_REGROWTH;
	}
	
	@Override
	protected void onZap( Ballistica bolt ) {
		
		for (int i : bolt.subPath(1, bolt.dist)) {
			int c = Dungeon.level.map[i];
			if (c == Terrain.EMPTY || 
				c == Terrain.EMBERS || 
				c == Terrain.EMPTY_DECO) {
				
				Level.set( i, Terrain.GRASS );
				
			}
		}
		
		int c = Dungeon.level.map[bolt.collisionPos];
		if (c == Terrain.EMPTY || 
			c == Terrain.EMBERS || 
			c == Terrain.EMPTY_DECO || 
			c == Terrain.GRASS ||
			c == Terrain.HIGH_GRASS) {
			
			GameScene.add( Blob.seed( bolt.collisionPos, (level() + 2) * 20, Regrowth.class ) );
			
		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//like vampiric enchantment, except with herbal healing buff

		int level = Math.max( 0, 0/*staff level*/ );

		// lvl 0 - 33%
		// lvl 1 - 43%
		// lvl 2 - 50%
		int maxValue = damage * (level + 2) / (level + 6);
		int effValue = Math.min( Random.IntRange(0, maxValue), attacker.HT - attacker.HP );

		Buff.affect(attacker, Sungrass.Health.class).boost( effValue );

	}

	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.foliage( curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"\"When life ceases new life always begins to grow... The eternal cycle always remains!\"";
	}
}
