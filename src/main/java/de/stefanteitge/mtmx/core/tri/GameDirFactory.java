/*
 * This file is part of mtmX.
 *
 * mtmX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mtmX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mtmX.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.stefanteitge.mtmx.core.tri;

import java.io.File;

import de.stefanteitge.mtmx.core.tri.internal.Blair1GameDir;
import de.stefanteitge.mtmx.core.tri.internal.BloodRayne1GameDir;
import de.stefanteitge.mtmx.core.tri.internal.BloodRayne2GameDir;
import de.stefanteitge.mtmx.core.tri.internal.BlowoutGameDir;
import de.stefanteitge.mtmx.core.tri.internal.CartGameDir;
import de.stefanteitge.mtmx.core.tri.internal.Evo1GameDir;
import de.stefanteitge.mtmx.core.tri.internal.Evo2GameDir;
import de.stefanteitge.mtmx.core.tri.internal.Fly2GameDir;
import de.stefanteitge.mtmx.core.tri.internal.Fury3GameDir;
import de.stefanteitge.mtmx.core.tri.internal.GhostbustersGameDir;
import de.stefanteitge.mtmx.core.tri.internal.HellbenderGameDir;
import de.stefanteitge.mtmx.core.tri.internal.Mtm1GameDir;
import de.stefanteitge.mtmx.core.tri.internal.Mtm2GameDir;
import de.stefanteitge.mtmx.core.tri.internal.NocturneGameDir;
import de.stefanteitge.mtmx.core.tri.internal.TvGameDir;


public class GameDirFactory {

	public static ITriGameDir create(File dir) {
		ITriGameDir gameDir;

		// TODO: make dynamic list

		// Blair Witch 1
		gameDir = Blair1GameDir.create(dir);
		if (gameDir != null) { return gameDir; }
		
		// BloodRayne
		gameDir = BloodRayne1GameDir.create(dir);
		if (gameDir != null) { return gameDir; }
		
		// BloodRayne 2
		gameDir = BloodRayne2GameDir.create(dir);
		if (gameDir != null) { return gameDir; }
		
		// Blowout
		gameDir = BlowoutGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// CART Precision Racing
		gameDir = CartGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// 4x4 Evo 1
		gameDir = Evo1GameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// 4x4 Evo2
		gameDir = Evo2GameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Fury3
		gameDir = Fury3GameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Hellbender
		gameDir = HellbenderGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Monster Truck Madness 1
		gameDir = Mtm1GameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Monster Truck Madness 2
		gameDir = Mtm2GameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Nocturne
		gameDir = NocturneGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Terminal Velocity
		gameDir = TvGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Ghostbusters
		gameDir = GhostbustersGameDir.create(dir);
		if (gameDir != null) { return gameDir; }

		// Fly! II
		gameDir = Fly2GameDir.create(dir);

		return gameDir;
	}
}
