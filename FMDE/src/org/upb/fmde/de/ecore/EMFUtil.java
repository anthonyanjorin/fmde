package org.upb.fmde.de.ecore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Adapted from the emoflon project (www.emoflon.org) to reduce dependencies.
 * 
 * @author anthonyanjorin
 */
public class EMFUtil {
	/**
	 * Creates a {@link ResourceSetImpl} and performs
	 * {@link #initializeDefault(ResourceSetImpl)} on it.
	 *
	 * @return the resource set
	 */
	public static final ResourceSet createDefaultResourceSet() {
		final ResourceSetImpl set = new ResourceSetImpl();
		initializeDefault(set);
		return set;
	}

	/**
	 * Performs the default initialization of the given {@link ResourceSet}
	 *
	 * @param set the {@link ResourceSet} to initialize
	 */
	public static void initializeDefault(final ResourceSet set) {
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());
	}

	/**
	 * Calculates the set of all outgoing references of a given EObject.
	 *
	 * @param object
	 * @return set of references
	 */
	public static Set<EStructuralFeature> getAllReferences(final EObject object) {
		final EList<EStructuralFeature> references = new BasicEList<>();

		// Collect outgoing cross references - excluding containment edges
		for (EContentsEList.FeatureIterator<?> featureIterator = (EContentsEList.FeatureIterator<?>) object
				.eCrossReferences().iterator(); featureIterator.hasNext();) {
			featureIterator.next();
			references.add(featureIterator.feature());
		}

		// Collect outgoing containment edges
		for (EContentsEList.FeatureIterator<?> featureIterator = (EContentsEList.FeatureIterator<?>) object.eContents()
				.iterator(); featureIterator.hasNext();) {
			featureIterator.next();
			references.add(featureIterator.feature());
		}
		return new HashSet<EStructuralFeature>(references);
	}

	/**
	 * Builds an identifier String for the given EObject. This identifier starts
	 * with
	 * <ul>
	 * <li>the attribute of the EObject as a String, if the EObject does only have
	 * one attribute.</li>
	 * <li>the attribute called 'name' of the EObject, if it has such an
	 * attribute</li>
	 * <li>any attribute of the EObject, but String attributes are preferred</li>
	 * </ul>
	 * The identifier ends with " : " followed by the type of the EObject. <br>
	 * Example: A MocaTree Node with the name "foo" will result in "foo : Node" <br>
	 * If the EObject does not have any attributes or all attributes have the value
	 * null, this function will only return the type of the EObject.
	 */
	public static String getIdentifier(final EObject eObject) {
		boolean success = false;
		List<EAttribute> attributes = eObject.eClass().getEAllAttributes();
		StringBuilder identifier = new StringBuilder();

		success = tryGetSingleAttribute(eObject, attributes, identifier);

		if (!success)
			success = tryGetNameAttribute(eObject, attributes, identifier);

		if (!success)
			success = tryGetAnyAttribute(eObject, attributes, identifier);

		if (success)
			identifier.append(" : ");

		identifier.append(eObject.eClass().getName());

		return identifier.toString();
	}

	/**
	 * @param name Use an empty StringBuilder as input. If this function returns
	 *             true, this parameter has been filled, if it returns false,
	 *             nothing happened.
	 * @return Indicates the success of this function and if the last parameter
	 *         contains output.
	 */
	private static boolean tryGetSingleAttribute(final EObject eObject, final List<EAttribute> attributes,
			final StringBuilder name) {
		boolean success = false;
		if (attributes.size() == 1) {
			Object obj = eObject.eGet(attributes.get(0));
			if (obj != null) {
				name.append(obj.toString());
				success = true;
			}
		}
		return success;
	}

	/**
	 * @param name Use an empty StringBuilder as input. If this function returns
	 *             true, this parameter has been filled, if it returns false,
	 *             nothing happened.
	 * @return Indicates the success of this function and if the last parameter
	 *         contains output.
	 */
	private static boolean tryGetNameAttribute(final EObject eObject, final List<EAttribute> attributes,
			final StringBuilder name) {
		boolean success = false;
		for (EAttribute feature : attributes) {
			if (feature.getName().equals("name")) {
				Object obj = eObject.eGet(feature);
				if (obj != null) {
					name.append(obj.toString());
					success = true;
					break;
				}
			}
		}
		return success;
	}

	/**
	 * @param name Use an empty StringBuilder as input. If this function returns
	 *             true, this parameter has been filled, if it returns false,
	 *             nothing happened.
	 * @return Indicates the success of this function and if the last parameter
	 *         contains output.
	 */
	private static boolean tryGetAnyAttribute(final EObject eObject, final List<EAttribute> attributes,
			final StringBuilder name) {
		boolean success = false;
		String nonStringName = null;
		String stringName = null;
		for (EAttribute feature : attributes) {
			Object obj = eObject.eGet(feature);
			if (obj == null)
				continue;
			if (obj instanceof String) {
				stringName = (String) obj;
				break;
			} else {
				nonStringName = obj.toString();
			}
		}
		if (stringName != null && !stringName.equals("null")) {
			name.append(stringName);
			success = true;
		} else if (nonStringName != null && !nonStringName.equals("null")) {
			name.append(nonStringName);
			success = true;
		}
		return success;
	}
}