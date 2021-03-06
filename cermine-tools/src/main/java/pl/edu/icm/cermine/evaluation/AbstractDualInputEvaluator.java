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

package pl.edu.icm.cermine.evaluation;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.icm.cermine.evaluation.AbstractEvaluator.Results;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.exception.TransformationException;

/**
 * Abstract evaluator used for implementation of evaluators that requires two
 * input files for each test case - one containing the document that needs to
 * be processed (actual document) and one containing expected result of the
 * processing (expected document).
 *
 * @author krusek
 * @param <P>
 * @param <R>
 */
public abstract class AbstractDualInputEvaluator<P, R extends Results<R>> extends AbstractEvaluator<P, R> {

    protected abstract Pattern getActualFilenamePattern();

    protected abstract String getExpectedFilenameReplacement();

    protected abstract P getExpectedDocument(Reader input) throws IOException;

    protected P getExpectedDocument(String path) throws IOException {
        Reader input = new FileReader(path);
        try {
            return getExpectedDocument(input);
        } finally {
            input.close();
        }
    }

    protected abstract P getActualDocument(Reader input) throws AnalysisException, TransformationException;

    protected P getActualDocument(String path) throws AnalysisException, TransformationException, IOException {
        Reader input = new FileReader(path);
        try {
            return getActualDocument(input);
        } finally {
            input.close();
        }
    }

    @Override
    protected Documents<P> getDocuments(String directory, String filename) throws IOException, AnalysisException, TransformationException {
        Matcher matcher = getActualFilenamePattern().matcher(filename);
        if (matcher.matches()) {
            StringBuffer buffer = new StringBuffer();
            matcher.appendReplacement(buffer, getExpectedFilenameReplacement());
            matcher.appendTail(buffer);
            P expectedDocument = getExpectedDocument(directory + buffer.toString());
            P actualDocument = getActualDocument(directory + filename);
            return new Documents<P>(expectedDocument, actualDocument);
        }
        else {
            return null;
        }
    }
}
