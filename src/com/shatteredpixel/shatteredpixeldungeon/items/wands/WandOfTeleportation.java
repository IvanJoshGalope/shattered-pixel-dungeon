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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfTeleportation extends Wand {

	{
		name = "Wand of Teleportation";
	}

	@Override
	protected void onZap( Ballistica bolt ) {
		
		Char ch = Actor.findChar( bolt.collisionPos );
		
		if (ch == curUser) {

			ScrollOfTeleportation.teleportHero( curUser );
			
		} else if (ch != null && !(ch instanceof NPC)) {
			
			int count = 10;
			int pos;
			do {
				pos = Dungeon.level.randomRespawnCell();
				if (count-- <= 0) {
					break;
				}
			} while (pos == -1);
			
			if (pos == -1) {
				
				GLog.w( ScrollOfTeleportation.TXT_NO_TELEPORT );
				
			} else {
			
				ch.pos = pos;
				ch.sprite.place( ch.pos );
				ch.sprite.visible = Dungeon.visible[pos];
				GLog.i( curUser.name + " teleported " + ch.name + " to somewhere" );
				
			}

		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

	}

	@Override
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.coldLight( curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"A blast from this wand will teleport a creature against " +
			"its will to a random place on the current level.";
	}
}
