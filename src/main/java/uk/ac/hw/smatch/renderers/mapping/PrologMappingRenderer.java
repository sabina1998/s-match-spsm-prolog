package uk.ac.hw.smatch.renderers.mapping;
import java.io.*;

import it.unitn.disi.smatch.data.mappings.IContextMapping;
import it.unitn.disi.smatch.data.mappings.IMappingElement;
import it.unitn.disi.smatch.data.trees.INode;
import it.unitn.disi.smatch.renderers.mapping.BaseFileMappingRenderer;
import it.unitn.disi.smatch.renderers.mapping.IMappingRenderer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Renders the mapping in Prolog-compatible format.
 * @author Tanya Howden
 * @author Kristof Kessler <kkessler@staffmail.ed.ac.uk>
 * @author unknown original author
 */
public class PrologMappingRenderer extends BaseFileMappingRenderer implements IMappingRenderer {

    public final static String PROLOG_FILES = "Prolog Files (*.pl)";

    protected void process(IContextMapping<INode> mapping, BufferedWriter out) throws IOException {
    	try{	
    		//write the results to file	
			out.write("similarity(" + Double.toString(mapping.getSimilarity()) + ").\n");

			for (IMappingElement<INode> mappingElement : mapping) {
				String sourceConceptName = getNodePathString(mappingElement.getSource());
				String targetConceptName = getNodePathString(mappingElement.getTarget());
				char relation = mappingElement.getRelation();

				out.write("match([[" + sourceConceptName + "]," + relation + ",[" + targetConceptName + "]]).\n");
			}
    	
    		//and also write it to file serialised as object
    		FileOutputStream fOut = new FileOutputStream("outputs/serialised-results.ser");
    		ObjectOutputStream outStream = new ObjectOutputStream(fOut);
    		
    		outStream.writeObject(mapping);
        	
    		outStream.close();
    		fOut.close();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    protected String getNodePathString(INode node) {
        StringBuilder sb = new StringBuilder();

        sb.insert(0, node.nodeData().getName());
        node = node.getParent();

        while (node != null) {
            sb.insert(0, node.nodeData().getName() + ",");
            node = node.getParent();
        }

        return sb.toString();
    }

    public String getDescription() {
        return PROLOG_FILES;
    }
}
