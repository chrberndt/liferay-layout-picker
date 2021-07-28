/**
 * 
 * Based on https://github.com/liferay/liferay-portal/blob/7.3.x/modules/apps/layout/layout-taglib/src/main/resources/META-INF/resources/select_layout/js/SelectLayout.es.js
 */
import {ClayButtonWithIcon} from '@clayui/button';
import {ClayInput} from '@clayui/form';
import ClayLayout from '@clayui/layout';
import ClayManagementToolbar from '@clayui/management-toolbar';
import {Treeview} from 'frontend-js-components-web';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

function visit(nodes, callback) {
	nodes.forEach((node) => {
		callback(node);

		if (node.children) {
			visit(node.children, callback);
		}
	});
}

/**
 * CustomSelectLayout
 *
 * This component shows a list of available layouts to select in expanded tree
 * and allows to filter them by searching.
 *
 * @review
 */

const CustomSelectLayout = ({
	followURLOnTitleClick,
	itemSelectorSaveEvent,
	multiSelection,
	namespace,
	nodes,
}) => {
	const [filterQuery, setFilterQuery] = useState();

	const handleSelectionChange = (selectedNodeIds) => {
		if (!selectedNodeIds.size) {
			return;
		}

		let data = [];

		visit(nodes, (node) => {
			if (selectedNodeIds.has(node.id)) {
				data.push({
					groupId: node.groupId,
					id: node.plid,
					layoutId: node.layoutId,
					name: node.value,
					titleMap: node.nameMap,
					plid: node.plid,
					privateLayout: node.privateLayout,
					value: node.url,
				});
			}
		});

		if (!multiSelection) {
			data = data[0];
		}

		if (followURLOnTitleClick) {
			Liferay.Util.getOpener().document.location.href = data.url;
		}
		else {
			Liferay.fire(itemSelectorSaveEvent, {
				data,
			});

			Liferay.Util.getOpener().Liferay.fire(itemSelectorSaveEvent, {
				data,
			});
		}
	};

	const empty = nodes.length === 0;

	return (
		<div className="select-layout">
			<ClayManagementToolbar>
				<ClayManagementToolbar.Search
					onSubmit={(event) => {
						event.preventDefault();
					}}
				>
					<ClayInput.Group>
						<ClayInput.GroupItem>
							<ClayInput
								className="form-control input-group-inset input-group-inset-after"
								disabled={empty}
								name={`${namespace}filterKeywords`}
								onInput={(event) => {
									setFilterQuery(
										event.target.value.toLowerCase()
									);
								}}
								placeholder={Liferay.Language.get('search-for')}
								type="text"
							/>
							<ClayInput.GroupInsetItem after tag="span">
								<ClayButtonWithIcon
									className="navbar-breakpoint-d-none"
									disabled={empty}
									displayType="unstyled"
									symbol="times"
								/>
								<ClayButtonWithIcon
									className="navbar-breakpoint-d-block"
									disabled={empty}
									displayType="unstyled"
									symbol="search"
								/>
							</ClayInput.GroupInsetItem>
						</ClayInput.GroupItem>
					</ClayInput.Group>
				</ClayManagementToolbar.Search>
			</ClayManagementToolbar>
			<ClayLayout.ContainerFluid
				className="layouts-selector"
				id={`${namespace}selectLayoutFm`}
			>
				<fieldset className="panel-body">
					{empty ? (
						<EmptyState />
					) : (
						<div
							className="layout-tree"
							id={`${namespace}layoutContainer`}
						>
							<Treeview
								NodeComponent={Treeview.Card}
								filterQuery={filterQuery}
								multiSelection={multiSelection}
								nodes={nodes}
								onSelectedNodesChange={handleSelectionChange}
							/>
						</div>
					)}
				</fieldset>
			</ClayLayout.ContainerFluid>
		</div>
	);
};

const EmptyState = () => {
	return (
		<div className="sheet taglib-empty-result-message">
			<div className="taglib-empty-result-message-header"></div>

			<div className="sheet-text text-center">
				{Liferay.Language.get('there-are-no-pages')}
			</div>
		</div>
	);
};

CustomSelectLayout.propTypes = {
	followURLOnTitleClick: PropTypes.bool,
	itemSelectorSaveEvent: PropTypes.string,
	multiSelection: PropTypes.bool,
	namespace: PropTypes.string,
	nodes: PropTypes.array.isRequired,
};

export default CustomSelectLayout;
