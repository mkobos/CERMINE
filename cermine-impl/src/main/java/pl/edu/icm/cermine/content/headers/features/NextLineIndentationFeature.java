/**
 * This file is part of CERMINE project.
 * Copyright (c) 2011-2013 ICM-UW
 *
 * CERMINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CERMINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with CERMINE. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.cermine.content.headers.features;

import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.tools.classification.general.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class NextLineIndentationFeature extends FeatureCalculator<BxLine, BxPage> {

    @Override
    public double calculateFeatureValue(BxLine line, BxPage page) {
        if (!line.hasNext()) {
            return 0.0;
        }
        
        int i = 0;
        BxLine l = line.getNext();
        double meanX = 0;
        while (l.hasNext() && i < 5) {
            l = l.getNext();
            if ((line.getX() < l.getX() && l.getX() < line.getX() + line.getWidth()) 
                    || (l.getX() < line.getX() && line.getX() < l.getX() + l.getWidth())) {
                meanX += l.getX();
                i++;
            } else {
                break;
            }
        }
        if (i == 0 || line.getWidth() == 0) {
            return 0.0;
        }
        
        meanX /= i;
        return Math.abs(line.getNext().getX() - meanX) / (double) line.getWidth();
    }
}
